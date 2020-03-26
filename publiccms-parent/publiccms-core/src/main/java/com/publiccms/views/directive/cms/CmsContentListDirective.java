package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.publiccms.views.pojo.entities.CmsContentStatistics;
import com.publiccms.views.pojo.query.CmsContentQuery;

/**
 *
 * CmsContentListDirective
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
        if (handler.getBoolean("advanced", false)) {
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
        queryEntity.setEmptyQuote(handler.getBoolean("emptyQuote"));
        queryEntity.setQuoteId(handler.getLong("quoteId"));
        queryEntity.setCategoryId(handler.getInteger("categoryId"));
        queryEntity.setCategoryIds(handler.getIntegerArray("categoryIds"));
        queryEntity.setModelIds(handler.getStringArray("modelId"));
        queryEntity.setParentId(handler.getLong("parentId"));
        queryEntity.setOnlyUrl(handler.getBoolean("onlyUrl"));
        queryEntity.setHasImages(handler.getBoolean("hasImages"));
        queryEntity.setHasFiles(handler.getBoolean("hasFiles"));
        queryEntity.setHasCover(handler.getBoolean("hasCover"));
        queryEntity.setUserId(handler.getLong("userId"));
        queryEntity.setStartPublishDate(handler.getDate("startPublishDate"));
        PageHandler page = service.getPage(queryEntity, handler.getBoolean("containChild"), handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        @SuppressWarnings("unchecked")
        List<CmsContent> list = (List<CmsContent>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            list.forEach(e -> {
                CmsContentStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                if (null != statistics) {
                    e.setClicks(e.getClicks() + statistics.getClicks());
                    e.setScores(e.getScores() + statistics.getScores());
                }
                if (absoluteURL) {
                    templateComponent.initContentUrl(site, e);
                    templateComponent.initContentCover(site, e);
                }
            });
        }
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentService service;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private StatisticsComponent statisticsComponent;
}