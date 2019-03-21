package com.publiccms.views.directive.cms;

// Generated 2015-7-10 16:36:23 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.cms.CmsTagTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsTagTypeListDirective
 * 
 */
@Component
public class CmsTagTypeListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getString("name"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", handler.getInteger("count", 20)));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsTagTypeService service;

}