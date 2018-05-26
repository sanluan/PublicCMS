package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.service.cms.CmsContentService;
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
        queryEntity.setSiteId(getSite(handler).getId());
        queryEntity.setEndPublishDate(handler.getDate("endPublishDate"));
        if (handler.getBoolean("advanced", false)) {
            queryEntity.setStatus(handler.getIntegerArray("status"));
            queryEntity.setDisabled(handler.getBoolean("disabled", false));
            queryEntity.setEmptyParent(handler.getBoolean("emptyParent"));
            queryEntity.setTitle(handler.getString("title"));
        } else {
            queryEntity.setStatus(new Integer[] { CmsContentService.STATUS_NORMAL });
            queryEntity.setDisabled(false);
            queryEntity.setEmptyParent(true);
            Date now = CommonUtils.getMinuteDate();
            if (null == queryEntity.getEndPublishDate() || queryEntity.getEndPublishDate().after(now)) {
                queryEntity.setEndPublishDate(now);
            }
        }
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
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentService service;

}