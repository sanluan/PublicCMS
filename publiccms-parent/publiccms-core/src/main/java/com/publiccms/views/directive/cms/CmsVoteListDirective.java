package com.publiccms.views.directive.cms;

// Generated 2020-3-26 12:04:23 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsVoteService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsVoteListDirective
 * 
 */
@Component
public class CmsVoteListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(),handler.getDate("startStartDate"), handler.getDate("endStartDate"), 
                handler.getDate("startEndDate"), handler.getDate("endEndDate"), handler.getString("title"), handler.getBoolean("disabled"), 
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex",1), handler.getInteger("pageSize",30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsVoteService service;

}