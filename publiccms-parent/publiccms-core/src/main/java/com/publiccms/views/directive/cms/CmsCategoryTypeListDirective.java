package com.publiccms.views.directive.cms;

// Generated 2016-2-26 15:57:04 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.cms.CmsCategoryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsCategoryTypeListDirective
 * 
 */
@Component
public class CmsCategoryTypeListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getInteger("pageIndex"),
                handler.getInteger("pageSize", handler.getInteger("count")));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsCategoryTypeService service;

}