package com.publiccms.views.directive.sys;

// Generated 2015-7-3 16:18:22 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysTaskListDirective
 * 
 */
@Component
public class SysTaskListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getInteger("status"),
                handler.getDate("beginUpdateDate"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysTaskService service;

}