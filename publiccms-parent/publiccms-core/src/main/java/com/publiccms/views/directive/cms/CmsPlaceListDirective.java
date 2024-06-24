package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.cms.CmsPlaceAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;

import freemarker.template.TemplateException;

/**
 *
 * placeList 推荐位列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>path</code>:页面片段路径
 * <li><code>userId</code>:发布用户id
 * <li><code>advanced</code>:开启高级选项, 默认为<code>false</code>
 * <li><code>status</code>:高级选项:数据状态,【0:操作,1:已发布,2:待审核】
 * <li><code>disabled</code>:高级选项:禁用状态,默认为<code>false</code>
 * <li><code>startPublishDate</code>:起始发布日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endPublishDate</code>:终止发布日期,高级选项禁用时不能超过现在,【2020-01-01
 * 23:59:59】,【2020-01-01】
 * <li><code>itemType</code>:数据项类型,【content:内容,category:分类,custom:自定义】
 * <li><code>itemId</code>:数据项id
 * <li><code>absoluteURL</code>:url、封面图处理为绝对路径 默认为<code>true</code>
 * <li><code>containsAttribute</code>:默认为<code>false</code>,http请求时为高级选项,为true时<code>place.attribute</code>为推荐位扩展数据<code>map</code>(字段编码,<code>value</code>)
 * <li><code>orderField</code>
 * 排序字段,【createDate:创建日期,clicks:点击数】,默认发布日期按orderType排序
 * <li><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为倒序
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsPlace}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.placeList path='/1.html' pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.placeList&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/cms/placeList?path=/1.html&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.page.totalCount);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsPlaceListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        SysSite site = getSite(handler);
        Date endPublishDate = handler.getDate("endPublishDate");
        Date expiryDate = null;
        String path = handler.getString("path");
        Boolean disabled = false;
        boolean containsAttribute = handler.getBoolean("containsAttribute", false);
        Integer[] status;
        if (getAdvanced(handler)) {
            status = handler.getIntegerArray("status");
            disabled = handler.getBoolean("disabled", false);
        } else {
            containsAttribute = !handler.inHttp();
            status = CmsPlaceService.STATUS_NORMAL_ARRAY;
            Date now = CommonUtils.getMinuteDate();
            if (null == endPublishDate || endPublishDate.after(now)) {
                endPublishDate = now;
            }
            expiryDate = now;
        }
        if (CommonUtils.notEmpty(path)) {
            path = path.replace("//", Constants.SEPARATOR);
        }
        PageHandler page = service.getPage(site.getId(), handler.getLong("userId"), path, handler.getString("itemType"),
                handler.getLong("itemId"), handler.getDate("startPublishDate"), endPublishDate, expiryDate, status, disabled,
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        @SuppressWarnings("unchecked")
        List<CmsPlace> list = (List<CmsPlace>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            Consumer<CmsPlace> consumer = null;
            if (containsAttribute) {
                Long[] ids = list.stream().map(CmsPlace::getId).toArray(Long[]::new);
                List<CmsPlaceAttribute> attributeList = attributeService.getEntitys(ids);
                Map<Long, CmsPlaceAttribute> attributeMap = CommonUtils.listToMap(attributeList, k -> k.getPlaceId());
                consumer = e -> {
                    Integer clicks = statisticsComponent.getPlaceClicks(e.getId());
                    if (null != clicks) {
                        e.setClicks(e.getClicks() + clicks);
                    }
                    if (absoluteURL) {
                        CmsUrlUtils.initPlaceUrl(site, e);
                        fileUploadComponent.initPlaceCover(site, e);
                    }
                    e.setAttribute(ExtendUtils.getAttributeMap(attributeMap.get(e.getId())));
                };
            } else {
                consumer = e -> {
                    Integer clicks = statisticsComponent.getPlaceClicks(e.getId());
                    if (null != clicks) {
                        e.setClicks(e.getClicks() + clicks);
                    }
                    if (absoluteURL) {
                        CmsUrlUtils.initPlaceUrl(site, e);
                        fileUploadComponent.initPlaceCover(site, e);
                    }
                };
            }
            list.forEach(consumer);
        }
        handler.put("page", page).render();
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private CmsPlaceService service;
    @Resource
    private CmsPlaceAttributeService attributeService;
    @Resource
    protected FileUploadComponent fileUploadComponent;
    @Resource
    private StatisticsComponent statisticsComponent;

}