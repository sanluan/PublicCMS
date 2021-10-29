package com.publiccms.views.directive.cms;

// Generated 2021-09-23 16:55:08 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.cms.CmsUserSurveyQuestionService;

/**
 *
 * CmsUserSurveyQuestionListDirective
 * 
 */
@Component
public class CmsUserSurveyQuestionListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("userId"), handler.getLong("questionId"),
                handler.getLong("surveyId"), handler.getString("orderField"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private CmsUserSurveyQuestionService service;

}