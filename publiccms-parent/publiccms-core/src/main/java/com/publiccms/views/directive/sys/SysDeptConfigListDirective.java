package com.publiccms.views.directive.sys;

// Generated 2018-8-11 10:47:32 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysDeptConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysDeptConfigListDirective
 * 
 */
@Component
public class SysDeptConfigListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("deptId"), handler.getString("config"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", handler.getInteger("count")));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysDeptConfigService service;

}