package com.publiccms.views.directive.cms;

// Generated 2016-3-3 17:43:34 by com.sanluan.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.cms.CmsVoteItemService;
import com.sanluan.common.handler.RenderHandler;
import com.sanluan.common.handler.PageHandler;

@Component
public class CmsVoteItemListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("voteId"), 
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex",1), handler.getInteger("count",30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsVoteItemService service;

}