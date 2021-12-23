package com.publiccms.views.directive.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsSurveyQuestion;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsSurveyQuestionService;

/**
 *
 * CmsSurveyQuestionDirective
 * 
 */
@Component
public class CmsSurveyQuestionWithoutAnswerDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsSurveyQuestion entity = service.getEntity(id);
            if (null != entity) {
                entity.setAnswer(null);
                if (absoluteURL) {
                    entity.setCover(TemplateComponent.getUrl(site, true, entity.getCover()));
                }
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsSurveyQuestion> entityList = service.getEntitys(ids);
                Consumer<CmsSurveyQuestion> consumer = e -> {
                    e.setAnswer(null);
                    if (absoluteURL) {
                        e.setCover(TemplateComponent.getUrl(site, true, e.getCover()));
                    }
                };
                Map<String, CmsSurveyQuestion> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), consumer, null);
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsSurveyQuestionService service;
}
