package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.FacetPageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.CmsContentStatistics;

/**
 *
 * CmsFacetSearchDirective
 * 
 */
@Component
public class CmsFacetSearchDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String word = handler.getString("word");
        Long[] tagIds = handler.getLongArray("tagId");
        String[] dictionaryValues = handler.getStringArray("dictionaryValues");
        if (CommonUtils.notEmpty(word) || CommonUtils.notEmpty(tagIds) || CommonUtils.notEmpty(dictionaryValues)) {
            SysSite site = getSite(handler);
            if (CommonUtils.notEmpty(word)) {
                statisticsComponent.search(site.getId(), word);
            }
            if (CommonUtils.notEmpty(tagIds)) {
                for (Long tagId : tagIds) {
                    statisticsComponent.searchTag(tagId);
                }
            }
            FacetPageHandler page;
            Integer pageIndex = handler.getInteger("pageIndex", 1);
            Integer count = handler.getInteger("pageSize", handler.getInteger("count", 30));
            Date currentDate = CommonUtils.getMinuteDate();
            boolean highlight = handler.getBoolean("highlight", false);
            String preTag = null;
            String postTag = null;
            if (highlight) {
                preTag = handler.getString("preTag");
                postTag = handler.getString("postTag");
            }
            try {
                page = service.facetQuery(handler.getBoolean("projection", false), handler.getBoolean("fuzzy", true), highlight,
                        site.getId(), word, handler.getStringArray("field"), tagIds, handler.getIntegerArray("categoryId"),
                        handler.getStringArray("modelId"), dictionaryValues, preTag, postTag, handler.getDate("startPublishDate"),
                        handler.getDate("endPublishDate", currentDate), currentDate, handler.getString("orderField"), pageIndex,
                        count);
                @SuppressWarnings("unchecked")
                List<CmsContent> list = (List<CmsContent>) page.getList();
                if (null != list) {
                    list.forEach(e -> {
                        CmsContentStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                        if (null != statistics) {
                            e.setClicks(e.getClicks() + statistics.getClicks());
                            e.setScores(e.getScores() + statistics.getScores());
                        }
                        templateComponent.initContentUrl(site, e);
                        templateComponent.initContentCover(site, e);
                    });
                }
            } catch (Exception e) {
                page = new FacetPageHandler(pageIndex, count, 0, null);
            }
            handler.put("page", page).render();
        }
    }

    @Autowired
    private StatisticsComponent statisticsComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsContentService service;

}