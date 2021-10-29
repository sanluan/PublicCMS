package com.publiccms.views.directive.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsSurveyQuestion;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsSurveyQuestionService;

/**
 *
 * CmsSurveyQuestionListDirective
 * 
 */
@Component
public class CmsSurveyQuestionWithoutAnswerListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        PageHandler page = service.getPage(handler.getLong("surveyId"), handler.getStringArray("questionTypes"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        SysSite site = getSite(handler);
        @SuppressWarnings("unchecked")
        List<CmsSurveyQuestion> list = (List<CmsSurveyQuestion>) page.getList();
        if (null != list) {
            list.forEach(e -> {
                e.setAnswer(null);
                if (absoluteURL) {
                    e.setCover(templateComponent.getUrl(site, true, e.getCover()));
                }
            });
        }

        handler.put("page", page).render();
    }

    @Autowired
    private CmsSurveyQuestionService service;
    @Autowired
    private TemplateComponent templateComponent;

}