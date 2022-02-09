package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:06 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.visit.VisitSessionService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * VisitSessionListDirective
 * 
 */
@Component
public class VisitSessionListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getString("sessionId"),
                handler.getDate("startVisitDate"), handler.getDate("endVisitDate"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private VisitSessionService service;

}