package com.publiccms.views.directive.sys;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysDeptCategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysDeptCategoryListDirective
 * 
 */
@Component
public class SysDeptCategoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("deptId"), handler.getInteger("categoryId"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize"));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysDeptCategoryService service;

}