package com.publiccms.views.directive.sys;

// Generated 2015-7-20 11:46:39 by SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.sys.SysDeptService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysDeptListDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("parentId"), handler.getInteger("userId"),
                handler.getString("name"), handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private SysDeptService service;

}