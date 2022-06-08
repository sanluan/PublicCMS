package com.publiccms.views.directive.log;

// Generated 2015-5-12 12:57:43 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.log.LogOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * LogOperateListDirective
 * 
 */
@Component
public class WorkloadDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getWorkLoadPage(getSite(handler).getId(), handler.getString("channel"),
                handler.getString("operate"), handler.getDate("startCreateDate"), handler.getDate("endCreateDate"),
                handler.getString("workloadType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private LogOperateService service;

}