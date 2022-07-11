package com.publiccms.views.directive.cms;

// Generated 2016-3-22 11:21:35 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import jakarta.annotation.Resource;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.cms.CmsWordService;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsWordListDirective
 * 
 */
@Component
public class CmsWordListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean hidden = false;
        String orderField = "searchCount";
        String name = null;
        if (getAdvanced(handler)) {
            hidden = handler.getBoolean("hidden");
            orderField = handler.getString("orderField");
            name = handler.getString("name");
        }
        PageHandler page = service.getPage(getSite(handler).getId(), hidden, handler.getDate("startCreateDate"),
                handler.getDate("endCreateDate"), name, orderField, handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Resource
    private CmsWordService service;

}