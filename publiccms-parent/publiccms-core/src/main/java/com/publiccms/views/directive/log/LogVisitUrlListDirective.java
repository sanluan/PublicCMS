package com.publiccms.views.directive.log;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.log.LogVisitUrlService;

/**
 *
 * LogVisitUrlListDirective
 * 
 */
@Component
public class LogVisitUrlListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getDate("startVisitDate"),
                handler.getDate("endVisitDate"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private LogVisitUrlService service;

}