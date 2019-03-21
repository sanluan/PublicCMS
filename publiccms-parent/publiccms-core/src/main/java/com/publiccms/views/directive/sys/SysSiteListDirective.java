package com.publiccms.views.directive.sys;

// Generated 2016-1-20 11:19:19 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysSiteListDirective
 * 
 */
@Component
public class SysSiteListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean disabled = false;
        if (handler.getBoolean("advanced", false)) {
            disabled = handler.getBoolean("disabled", false);
        }
        PageHandler page = service.getPage(disabled,handler.getShort("parentId"), handler.getString("name"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysSiteService service;

}