package com.publiccms.views.directive.cms;

// Generated 2016-1-25 10:53:21 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;
import com.sanluan.common.handler.PageHandler;

@Component
public class CmsContentRelatedListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getLong("contentId"), handler.getLong("relatedContentId"),
                handler.getLong("userId"), handler.getString("orderField"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("count", 10));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentRelatedService service;

}