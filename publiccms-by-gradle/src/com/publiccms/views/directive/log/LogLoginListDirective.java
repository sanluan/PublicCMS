package com.publiccms.views.directive.log;

// Generated 2015-5-12 12:57:43 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class LogLoginListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("userId"),
                handler.getDate("startCreateDate"), handler.getDate("endCreateDate"), handler.getString("channel"),
                handler.getBoolean("result"), handler.getString("name"), handler.getString("ip"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("count", 20));
        handler.put("page", page).render();
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private LogLoginService service;

}