package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.base.HighLighterQuery;
import com.publiccms.common.handler.FacetPageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.ClickStatistics;

/**
 *
 * facetSearch 内容列表查询指令
 * <p>
 * 参数列表{@link com.publiccms.views.directive.cms.CmsSearchDirective}
 * 
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>
 * 分面搜索结果{@link com.publiccms.common.handler.FacetPageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsContent}
 * </ul>
 * 使用示例
 * 
 * <pre>
  &lt;@cms.facetSearch word='cms' pageSize=10&gt; 
  &lt;p&gt; category: &lt;#list page.facetMap.categoryId as k,v&gt;&lt;@cms.category id=k&gt;${object.name}&lt;/@cms.category&gt;(${v})&lt;/#list&gt;&lt;/p&gt;
  &lt;p&gt; model: &lt;#list page.facetMap.modelId as k,v&gt;&lt;@cms.model id=k&gt;${object.name}&lt;/@cms.model&gt;(${v})&lt;/#list&gt;&lt;/p&gt;
  &lt;#list page.list as a&gt;&lt;p&gt;${a.title}&lt;/p&gt;&lt;/#list&gt;
  &lt;/@cms.facetSearch&gt;
 * </pre>
 * 
 * <pre>
  &lt;script&gt;
   $.getJSON('//cms.publiccms.com/api/directive/cms/facetSearch?word=cms&amp;pageSize=10', function(data){    
     console.log(data.totalCount);
   });
   &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class CmsFacetSearchDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String word = handler.getString("word");
        Long[] tagIds = handler.getLongArray("tagIds");
        if (null == tagIds) {
            tagIds = handler.getLongArray("tagId");
        }
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(word)) {
            word = CommonUtils.keep(word, 100, null);
            statisticsComponent.search(site.getId(), word);
        }
        if (CommonUtils.notEmpty(tagIds)) {
            for (Long tagId : tagIds) {
                statisticsComponent.searchTag(tagId);
            }
        }
        FacetPageHandler page;
        Integer pageIndex = handler.getInteger("pageIndex", 1);
        Integer pageSize = handler.getInteger("pageSize", handler.getInteger("count", 30));
        Date currentDate = CommonUtils.getMinuteDate();
        HighLighterQuery highLighterQuery = new HighLighterQuery(handler.getBoolean("highlight", false));
        if (highLighterQuery.isHighlight()) {
            highLighterQuery.setPreTag(handler.getString("preTag"));
            highLighterQuery.setPostTag(handler.getString("postTag"));
        }
        try {
            page = service.facetQuery(site.getId(), handler.getBoolean("projection", false), handler.getBoolean("phrase", false),
                    highLighterQuery, word, handler.getString("exclude"), handler.getStringArray("fields"), tagIds,
                    handler.getInteger("categoryId"), handler.getBoolean("containChild"), handler.getIntegerArray("categoryIds"),
                    handler.getStringArray("modelIds"), handler.getStringArray("extendsValues"),
                    handler.getStringArray("dictionaryValues"), handler.getBoolean("dictionaryUnion"),
                    handler.getDate("startPublishDate"), handler.getDate("endPublishDate", currentDate), currentDate,
                    handler.getString("orderField"), pageIndex, pageSize);
            @SuppressWarnings("unchecked")
            List<CmsContent> list = (List<CmsContent>) page.getList();
            if (null != list) {
                list.forEach(e -> {
                    ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                    if (null != statistics) {
                        e.setClicks(e.getClicks() + statistics.getClicks());
                    }
                    TemplateComponent.initContentUrl(site, e);
                    TemplateComponent.initContentCover(site, e);
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            page = new FacetPageHandler(pageIndex, pageSize);
        }
        handler.put("page", page).render();
    }

    @Autowired
    private StatisticsComponent statisticsComponent;
    @Autowired
    private CmsContentService service;

}