package org.publiccms.views.directive.sys;

// Generated 2016-3-1 17:24:12 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.service.sys.SysAppTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysAppTokenListDirective
 * 
 */
@Component
public class SysAppTokenListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("appId"), handler.getInteger("pageIndex", 1),
                handler.getInteger("count", 30));
        handler.put("page", page).render();
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysAppTokenService service;

}