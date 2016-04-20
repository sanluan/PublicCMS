package com.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.sys.SysRoleMoudleService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysRoleMoudleListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("roleId"), handler.getInteger("moudleId"),
                handler.getInteger("pageIndex", 1), handler.getInteger("count"));
        handler.put("page", page).render();
    }

    @Autowired
    private SysRoleMoudleService service;

}