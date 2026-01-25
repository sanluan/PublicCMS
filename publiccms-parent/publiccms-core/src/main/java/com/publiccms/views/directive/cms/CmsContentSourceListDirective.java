package com.publiccms.views.directive.cms;

// Generated 2026-1-25 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsContentSourceService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

import freemarker.template.TemplateException;
/**
 *
 * CmsContentSourceListDirective
 * 
 */
@Component
public class CmsContentSourceListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(getSite(handler).getId(),handler.getString("name"), 
                handler.getLong("userId"), 
                handler.getString("orderType"), handler.getInteger("pageIndex",1), handler.getInteger("pageSize",30));
        handler.put("page", page).render();
    }

    @Resource
    private CmsContentSourceService service;

}