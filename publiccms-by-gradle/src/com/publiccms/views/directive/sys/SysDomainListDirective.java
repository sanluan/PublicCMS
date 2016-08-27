package com.publiccms.views.directive.sys;

// Generated 2016-1-20 11:19:18 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.sys.SysDomainService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysDomainListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer siteId = null;
        if (handler.getBoolean("advanced", false)) {
            siteId = handler.getInteger("siteId");
        } else {
            siteId = getSite(handler).getId();
        }
        PageHandler page = service.getPage(siteId, handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private SysDomainService service;

}