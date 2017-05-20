package org.publiccms.views.directive.home;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.service.home.HomeAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * HomeAttentionListDirective
 * 
 */
@Component
public class HomeAttentionListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getLong("userId"), handler.getLong("attentionId"),
                handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }
    
    @Override
    public boolean needUserToken() {
        return true;
    }

    @Autowired
    private HomeAttentionService service;

}