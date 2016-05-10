package com.publiccms.views.directive.cms;

// Generated 2016-3-22 11:21:35 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.cms.CmsWordService;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsWordListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean hidden = false;
        if (handler.getBoolean("advanced", false)) {
            hidden = handler.getBoolean("hidden");
        }
        PageHandler page = service.getPage(getSite(handler).getId(), hidden, handler.getDate("startCreateDate"),
                handler.getDate("endCreateDate"), handler.getString("name"), handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsWordService service;

}