package org.publiccms.views.directive.sys;

// Generated 2016-1-20 11:19:18 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.service.sys.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysUserTokenListDirective
 * 
 */
@Component
public class SysUserTokenListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("userId"), handler.getString("channel"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }
    
    @Override
    public boolean needUserToken() {
        return true;
    }

    @Autowired
    private SysUserTokenService service;

}