package com.publiccms.views.directive.cms;

// Generated 2021-09-23 16:55:08 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.cms.CmsUserSurveyService;

/**
 *
 * CmsUserSurveyListDirective
 * 
 */
@Component
public class CmsUserSurveyListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("userId"), handler.getLong("surveyId"),
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private CmsUserSurveyService service;

}