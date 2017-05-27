package org.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.service.sys.SysMoudleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysMoudleListDirective
 * 
 */
@Component
public class SysMoudleListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean menu = null;
        if (!handler.getBoolean("advanced", false)) {
            menu = handler.getBoolean("menu", true);
        }
        PageHandler page = service.getPage(handler.getInteger("parentId"), menu, handler.getInteger("pageIndex", 1),
                handler.getInteger("count", 20));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysMoudleService service;

}