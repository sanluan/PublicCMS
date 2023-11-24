package com.publiccms.views.directive.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.logic.service.sys.SysWorkflowStepService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

import freemarker.template.TemplateException;
/**
 *
 * SysWorkflowStepListDirective
 * 
 */
@Component
public class SysWorkflowStepListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(handler.getInteger("workflowId"), 
                handler.getString("orderType"), handler.getInteger("pageIndex",1), handler.getInteger("pageSize",30));
        handler.put("page", page).render();
    }

    @Resource
    private SysWorkflowStepService service;

}