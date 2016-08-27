package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.StatisticsComponent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.sanluan.common.handler.FacetPageHandler;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsFacetSearchDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String word = handler.getString("word");
        String tagId = handler.getString("tagId");
        if (notEmpty(word) || notEmpty(tagId)) {
            SysSite site = getSite(handler);
            if (notEmpty(word)) {
                statisticsComponent.search(site.getId(), word);
            }
            if (notEmpty(tagId)) {
                try {
                    statisticsComponent.searchTag(Long.parseLong(tagId));
                } catch (NumberFormatException e) {
                }
            }
            PageHandler page;
            Integer pageIndex = handler.getInteger("pageIndex", 1);
            Integer count = handler.getInteger("count", 30);
            try {
                page = service.facetQuery(site.getId(), handler.getString("categoryId"), handler.getString("modelId"), word,
                        tagId, pageIndex, count);
            } catch (Exception e) {
                page = new FacetPageHandler(pageIndex, count, 0, null);
            }
            handler.put("page", page).render();
        }
    }

    @Autowired
    private StatisticsComponent statisticsComponent;
    @Autowired
    private CmsContentService service;

}