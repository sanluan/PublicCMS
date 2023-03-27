package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;
import java.util.List;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.ClickStatistics;
import com.publiccms.views.pojo.query.CmsContentQuery;

/**
 *
 * contentList 内容列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>categoryId</code>:分类id,当parentId为空时有效
 * <li><code>containChild</code>:是否包含子分类,【true,false】
 * <li><code>categoryIds</code>:多个分类id,当categoryId为空时有效
 * <li><code>modelId</code>:多个模型id
 * <li><code>parentId</code>:父内容id
 * <li><code>onlyUrl</code>:外链,【true,false】
 * <li><code>hasImages</code>:拥有图片列表,【true,false】
 * <li><code>hasFiles</code>:拥有附件列表,【true,false】
 * <li><code>hasProducts</code>:拥有产品列表,【true,false】
 * <li><code>hasCover</code>:拥有封面图,【true,false】
 * <li><code>userId</code>:发布用户id
 * <li><code>startPublishDate</code>:起始发布日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endPublishDate</code>:终止发布日期,高级选项禁用时不能超过现在,【2020-01-01
 * 23:59:59】,【2020-01-01】
 * <li><code>advanced</code>:开启高级选项, 默认为<code>false</code>
 * <li><code>status</code>:高级选项:内容状态,【0:操作,1:已发布,2:待审核,3:驳回】
 * <li><code>disabled</code>:高级选项:禁用状态,默认为<code>false</code>
 * <li><code>emptyParent</code>:高级选项:父内容id是否为空,【true,false】,当parentId为空时有效
 * <li><code>title</code>:高级选项:标题
 * <li><code>absoluteURL</code>:url处理为绝对路径 默认为<code>true</code>
 * <li><code>absoluteId</code>:id处理为引用内容的ID 默认为<code>true</code>
 * <li><code>orderField</code>
 * 排序字段,【score:评分,comments:评论数,clicks:点击数,publishDate:发布日期,updateDate:更新日期,checkDate:审核日期】,默认置顶级别倒序、发布日期按orderType排序
 * <li><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为倒序
 * <li><code>firstResult</code>:开始位置,从1开始
 * <li><code>pageIndex</code>:页码,firstResult不存在时有效
 * <li><code>pageSize</code>:每页条数
 * <li><code>maxResults</code>:最大结果数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsContent}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.contentList pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.contentList&gt;
 * 
 * <pre>
 *  &lt;script&gt;
    $.getJSON('${site.dynamicPath}api/directive/cms/contentList?pageSize=10', function(data){    
      console.log(data.totalCount);
    });
    &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class CmsContentListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        CmsContentQuery queryEntity = new CmsContentQuery();
        SysSite site = getSite(handler);
        queryEntity.setSiteId(site.getId());
        queryEntity.setEndPublishDate(handler.getDate("endPublishDate"));
        if (getAdvanced(handler)) {
            queryEntity.setStatus(handler.getIntegerArray("status"));
            queryEntity.setDisabled(handler.getBoolean("disabled", false));
            queryEntity.setEmptyParent(handler.getBoolean("emptyParent"));
            queryEntity.setTitle(handler.getString("title"));
        } else {
            queryEntity.setStatus(CmsContentService.STATUS_NORMAL_ARRAY);
            queryEntity.setDisabled(false);
            queryEntity.setEmptyParent(true);
            Date now = CommonUtils.getMinuteDate();
            if (null == queryEntity.getEndPublishDate() || queryEntity.getEndPublishDate().after(now)) {
                queryEntity.setEndPublishDate(now);
            }
            queryEntity.setExpiryDate(now);
        }
        queryEntity.setCategoryId(handler.getInteger("categoryId"));
        queryEntity.setCategoryIds(handler.getIntegerArray("categoryIds"));
        queryEntity.setModelIds(handler.getStringArray("modelId"));
        queryEntity.setParentId(handler.getLong("parentId"));
        queryEntity.setOnlyUrl(handler.getBoolean("onlyUrl"));
        queryEntity.setHasImages(handler.getBoolean("hasImages"));
        queryEntity.setHasFiles(handler.getBoolean("hasFiles"));
        queryEntity.setHasProducts(handler.getBoolean("hasProducts"));
        queryEntity.setHasCover(handler.getBoolean("hasCover"));
        queryEntity.setUserId(handler.getLong("userId"));
        queryEntity.setDeptId(handler.getInteger("deptId"));
        queryEntity.setStartPublishDate(handler.getDate("startPublishDate"));
        PageHandler page = service.getPage(queryEntity, handler.getBoolean("containChild"), handler.getString("orderField"),
                handler.getString("orderType"),handler.getInteger("firstResult"), handler.getInteger("pageIndex", 1), 
                handler.getInteger("pageSize", handler.getInteger("count", 30)), handler.getInteger("maxResults"));
        @SuppressWarnings("unchecked")
        List<CmsContent> list = (List<CmsContent>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            boolean absoluteId = handler.getBoolean("absoluteId", true);
            list.forEach(e -> {
                ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                if (null != statistics) {
                    e.setClicks(e.getClicks() + statistics.getClicks());
                }
                if (absoluteId && null==e.getParentId() && null != e.getQuoteContentId()) {
                    e.setId(e.getQuoteContentId());
                }
                if (absoluteURL) {
                    TemplateComponent.initContentUrl(site, e);
                    TemplateComponent.initContentCover(site, e);
                }
            });
        }
        handler.put("page", page).render();
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Resource
    private CmsContentService service;
    @Resource
    private StatisticsComponent statisticsComponent;
}