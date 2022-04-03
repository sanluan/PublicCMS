package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:43:59 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.visit.VisitHistoryService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * VisitHistoryListDirective
 * 
 */
@Component
public class VisitHistoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getString("sessionId"),
                handler.getDate("startCreateDate"), handler.getDate("endCreateDate"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private VisitHistoryService service;

}