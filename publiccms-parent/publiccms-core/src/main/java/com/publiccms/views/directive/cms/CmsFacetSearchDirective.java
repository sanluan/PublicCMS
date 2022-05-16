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
 * 参数列表
 * <ul>
 * <li><code>word</code> 搜索词,多个搜索词时取并集结果
 * <li><code>tagId</code> 多个标签id,多个标签时取并集结果
 * <li><code>categoryId</code> 分类id
 * <li><code>containChild</code> 包含子分类，当categoryId不为空时有效
 * <li><code>categoryIds</code> 多个分类id，当categoryId为空时有效
 * <li><code>dictionaryValues</code>
 * 多个数据字典值,只有父级值时包含所有子级结果,格式：[字段编码]_{字段值],例如:dictionaryValues='extend1_value1,extend1_value1'
 * <li><code>dictionaryUnion</code>
 * 取数据字典并集结果,dictionaryUnion不为空时有效,【true,false】,默认为交集结果
 * <li><code>highlight</code> 高亮关键词,【true,false】,默认为false,启用高亮后台
 * 标题、作者、编辑、描述字段应该加?no_esc以使高亮html生效,cms会自动对原值有进行html安全转义
 * <li><code>preTag</code> 高亮前缀,启用高亮时有效,默认为"&lt;B&gt;"
 * <li><code>postTag</code> 高亮后缀,启用高亮时有效,默认为"&lt;/B&gt;"
 * <li><code>projection</code> 投影结果,【true,false】,默认为false
 * <li><code>phrase</code> 精确搜索,【true,false】,默认为false
 * <li><code>fields</code> 搜索字段,【title:标题, author:作者, editor:编辑, description:描述,
 * text:正文】
 * <li><code>modelIds</code> 多个模型id
 * <li><code>startPublishDate</code> 开始发布日期,【2000-01-01 23:59:59】,【2000-01-01】
 * <li><code>endPublishDate</code> 结束发布日期,【2000-01-01 23:59:59】,【2000-01-01】
 * <li><code>orderField</code> 排序字段,【clicks:点击数倒叙,publishDate:发布日期倒叙】,默认相关度倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
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
  &lt;@_facetSearch word='cms' pageSize=10&gt; 
  &lt;p&gt; category: &lt;#list page.facetMap.categoryId as k,v&gt;&lt;@_category id=k&gt;${object.name}&lt;/@_category&gt;(${v})&lt;/#list&gt;&lt;/p&gt;
  &lt;p&gt; model: &lt;#list page.facetMap.modelId as k,v&gt;&lt;@_model id=k&gt;${object.name}&lt;/@_model&gt;(${v})&lt;/#list&gt;&lt;/p&gt;
  &lt;#list page.list as a&gt;&lt;p&gt;${a.title}&lt;/p&gt;&lt;/#list&gt;
  &lt;/@_facetSearch&gt;
 * </pre>
 * 
 * <pre>
  &lt;script&gt;
   $.getJSON('//cms.publiccms.com/api/directive/facetSearch?word=cms&amp;pageSize=10', function(data){    
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
            if (word.length() > 200) {
                word = word.substring(0, 200);
            }
            statisticsComponent.search(site.getId(), word);
        }
        if (CommonUtils.notEmpty(tagIds)) {
            for (Long tagId : tagIds) {
                statisticsComponent.searchTag(tagId);
            }
        }
        FacetPageHandler page;
        Integer pageIndex = handler.getInteger("pageIndex", 1);
        Integer pageSize = handler.getInteger("pageSize", 30);
        Date currentDate = CommonUtils.getMinuteDate();
        HighLighterQuery highLighterQuery = new HighLighterQuery(handler.getBoolean("highlight", false));
        if (highLighterQuery.isHighlight()) {
            highLighterQuery.setPreTag(handler.getString("preTag"));
            highLighterQuery.setPostTag(handler.getString("postTag"));
        }
        try {
            page = service.facetQuery(site.getId(), handler.getBoolean("projection", false), handler.getBoolean("phrase", false),
                    highLighterQuery, word, handler.getStringArray("fields"), tagIds, handler.getInteger("categoryId"),
                    handler.getBoolean("containChild"), handler.getIntegerArray("categoryIds"),
                    handler.getStringArray("modelIds"), handler.getStringArray("dictionaryValues"),
                    handler.getBoolean("dictionaryUnion"), handler.getDate("startPublishDate"),
                    handler.getDate("endPublishDate", currentDate), currentDate, handler.getString("orderField"), pageIndex,
                    pageSize);
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