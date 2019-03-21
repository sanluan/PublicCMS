package com.publiccms.views.directive.sys;

// Generated 2016-1-20 11:19:18 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysDomainListDirective
 * 
 */
@Component
public class SysDomainListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Short siteId = null;
        if (handler.getBoolean("advanced", false)) {
            siteId = handler.getShort("siteId");
        } else {
            siteId = getSite(handler).getId();
        }
        PageHandler page = service.getPage(siteId, handler.getBoolean("wild"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysDomainService service;

}