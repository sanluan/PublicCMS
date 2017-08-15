package org.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.site.StatisticsComponent;
import org.publiccms.logic.service.cms.CmsContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.FacetPageHandler;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

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
        String tagId = handler.getString("tagId");
        if (notEmpty(word) || notEmpty(tagId)) {
            SysSite site = getSite(handler);
            if (notEmpty(word)) {
                statisticsComponent.search(site.getId(), word);
            }
            String[] tagIds = handler.getStringArray("tagId");
            if (notEmpty(tagIds)) {
                for (String id : tagIds) {
                    try {
                        statisticsComponent.searchTag(Long.parseLong(id));
                    } catch (NumberFormatException e) {
                    }
                }
            }
            PageHandler page;
            Integer pageIndex = handler.getInteger("pageIndex", 1);
            Integer count = handler.getInteger("count", 30);
            try {
                page = service.facetQuery(site.getId(), handler.getStringArray("categoryId"), handler.getStringArray("modelId"),
                        handler.getStringArray("userId"), word, tagId, pageIndex, count);
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