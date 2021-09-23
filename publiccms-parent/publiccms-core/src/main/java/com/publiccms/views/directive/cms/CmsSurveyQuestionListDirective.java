package com.publiccms.views.directive.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsSurveyQuestionService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsSurveyQuestionListDirective
 * 
 */
@Component
public class CmsSurveyQuestionListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getLong("surveyId"), handler.getStringArray("modelId"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsSurveyQuestionService service;

}