package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsCategoryModelListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("modelId"), handler.getInteger("categoryId"),
                handler.getInteger("pageIndex", 1), handler.getInteger("count"));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsCategoryModelService service;

}