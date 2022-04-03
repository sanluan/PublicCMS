package com.publiccms.views.directive.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsSurveyQuestionItem;
import com.publiccms.logic.service.cms.CmsSurveyQuestionItemService;

/**
 *
 * CmsSurveyQuestionItemDirective
 * 
 */
@Component
public class CmsSurveyQuestionItemDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        if (CommonUtils.notEmpty(id)) {
            CmsSurveyQuestionItem entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsSurveyQuestionItem> entityList = service.getEntitys(ids);
                Map<String, CmsSurveyQuestionItem> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsSurveyQuestionItemService service;

}
