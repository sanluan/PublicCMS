package com.publiccms.views.directive.cms;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsContentTextHistoryService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsContentHistoryListDirective
 * 
 */
@Component
public class CmsContentTextHistoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getLong("contentId"), handler.getString("fieldName"),
                handler.getLong("userId"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentTextHistoryService service;

}