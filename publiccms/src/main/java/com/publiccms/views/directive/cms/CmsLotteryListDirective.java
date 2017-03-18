package com.publiccms.views.directive.cms;

// Generated 2016-3-1 17:24:24 by com.sanluan.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.cms.CmsLotteryService;
import com.sanluan.common.handler.RenderHandler;
import com.sanluan.common.handler.PageHandler;

@Component
public class CmsLotteryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean disabled = false;
        if (handler.getBoolean("advanced", false)) {
            disabled = handler.getBoolean("disabled", false);
        }
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getDate("startStartDate"),
                handler.getDate("endStartDate"), handler.getDate("startEndDate"), handler.getDate("endEndDate"), disabled,
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsLotteryService service;

}