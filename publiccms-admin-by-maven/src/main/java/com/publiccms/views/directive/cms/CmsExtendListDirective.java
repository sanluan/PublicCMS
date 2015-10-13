package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsExtendService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsExtendListDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("itemType"),handler.getInteger("itemId"), handler.getInteger("extendType"),
                handler.getBoolean("isCustom"), handler.getInteger("pageIndex"), handler.getInteger("count"));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsExtendService service;

}