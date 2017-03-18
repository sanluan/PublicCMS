package com.publiccms.views.directive.cms;

// Generated 2016-11-20 14:50:55 by com.sanluan.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;
import com.sanluan.common.handler.RenderHandler;
import com.sanluan.common.handler.PageHandler;

@Component
public class CmsDictionaryDataListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(
                handler.getInteger("pageIndex",1), handler.getInteger("count",30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsDictionaryDataService service;

}