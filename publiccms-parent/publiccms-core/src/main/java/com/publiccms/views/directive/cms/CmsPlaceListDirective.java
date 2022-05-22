package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceService;

/**
 *
 * placeList 推荐位列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>path</code> 页面片段路径
 * <li><code>userId</code> 发布用户id
 * <li><code>advanced</code> 开启高级选项， 默认为<code> false</code>
 * <li><code>status</code> 高级选项:数据状态，【0:操作,1:已发布,2:待审核】
 * <li><code>disabled</code> 高级选项:禁用状态，默认为<code>false</code>
 * <li><code>startPublishDate</code> 发布日期开始时间,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endPublishDate</code> 发布日期截至时间，高级选项禁用时不能超过现在,【2020-01-01
 * 23:59:59】,【2020-01-01】
 * <li><code>itemType</code> 数据项类型,【content:内容,category:分类,custom:自定义】
 * <li><code>itemId</code> 数据项id
 * <li><code>absoluteURL</code> url、封面图处理为绝对路径 默认为<code>true</code>
 * <li><code>orderField</code>
 * 排序字段,【createDate:创建日期,clicks:点击数】,默认发布日期按orderType排序
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】，默认为倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsPlace}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.placeList path='/1.html' pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.placeList&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('//cms.publiccms.com/api/directive/cms/placeList?path=/1.html&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.totalCount);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsPlaceListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        Date endPublishDate = handler.getDate("endPublishDate");
        Date expiryDate = null;
        String path = handler.getString("path");
        Boolean disabled = false;
        Integer[] status;
        if (handler.getBoolean("advanced", false)) {
            status = handler.getIntegerArray("status");
            disabled = handler.getBoolean("disabled", false);
        } else {
            status = CmsPlaceService.STATUS_NORMAL_ARRAY;
            Date now = CommonUtils.getMinuteDate();
            if (null == endPublishDate || endPublishDate.after(now)) {
                endPublishDate = now;
            }
            expiryDate = now;
        }
        if (CommonUtils.notEmpty(path)) {
            path = path.replace("//", CommonConstants.SEPARATOR);
        }
        PageHandler page = service.getPage(site.getId(), handler.getLong("userId"), path, handler.getString("itemType"),
                handler.getLong("itemId"), handler.getDate("startPublishDate"), endPublishDate, expiryDate, status, disabled,
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        @SuppressWarnings("unchecked")
        List<CmsPlace> list = (List<CmsPlace>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            list.forEach(e -> {
                Integer clicks = statisticsComponent.getPlaceClicks(e.getId());
                if (null != clicks) {
                    e.setClicks(e.getClicks() + clicks);
                }
                if (absoluteURL) {
                    templateComponent.initPlaceCover(site, e);
                    e.setUrl(TemplateComponent.getUrl(site, site.isUseStatic(), e.getUrl()));
                }
            });
        }
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private CmsPlaceService service;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private StatisticsComponent statisticsComponent;

}