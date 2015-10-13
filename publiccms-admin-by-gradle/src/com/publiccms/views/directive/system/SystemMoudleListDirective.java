package com.publiccms.views.directive.system;

// Generated 2015-7-22 13:48:39 by SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.system.SystemMoudleService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SystemMoudleListDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("parentId"), handler.getString("url"), 
                handler.getInteger("pageIndex",1), handler.getInteger("count",20));
        handler.put("page", page).render();
    }

    @Autowired
    private SystemMoudleService service;

}