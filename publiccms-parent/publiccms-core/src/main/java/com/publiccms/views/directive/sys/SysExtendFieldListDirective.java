package com.publiccms.views.directive.sys;

// Generated 2016-3-2 13:39:54 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysExtendFieldListDirective
 * 
 */
@Component
public class SysExtendFieldListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer extendId = handler.getInteger("extendId");
        handler.put("list", service.getList(extendId)).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysExtendFieldService service;

}