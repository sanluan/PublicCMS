package com.publiccms.views.directive.sys;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysDeptPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysDeptPageListDirective
 * 
 */
@Component
public class SysDeptPageListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("deptId"), handler.getString("page"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", handler.getInteger("count")));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysDeptPageService service;

}