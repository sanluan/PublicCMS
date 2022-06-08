package com.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysRoleModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysRoleModuleListDirective
 * 
 */
@Component
public class SysRoleModuleListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("roleId"), handler.getString("moduleId"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", handler.getInteger("count")));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysRoleModuleService service;

}