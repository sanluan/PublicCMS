package com.publiccms.views.directive.cms;

// Generated 2021-09-23 16:55:08 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsUserSurvey;
import com.publiccms.entities.cms.CmsUserSurveyId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsUserSurveyService;

/**
 *
 * CmsUserSurveyDirective
 * 
 */
@Component
public class CmsUserSurveyDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long userId = handler.getLong("userId");
        Long surveyId = handler.getLong("surveyId");
        SysSite site = getSite(handler);
        if (null != userId) {
            if (null != surveyId) {
                CmsUserSurvey entity = service.getEntity(new CmsUserSurveyId(userId, surveyId));
                if (null != entity && site.getId() == entity.getSiteId()) {
                    handler.put("object", entity).render();
                }
            } else {
                Long[] surveyIds = handler.getLongArray("surveyIds");
                if (CommonUtils.notEmpty(surveyIds)) {
                    CmsUserSurveyId[] entityIds = new CmsUserSurveyId[surveyIds.length];
                    for (int i = 0; i < surveyIds.length; i++) {
                        entityIds[i] = new CmsUserSurveyId(userId, surveyIds[i]);
                    }
                    List<CmsUserSurvey> entityList = service.getEntitys(entityIds);
                    Map<String, CmsUserSurvey> map = CommonUtils.listToMap(entityList,
                            k -> String.valueOf(k.getId().getSurveyId()), null, entity -> site.getId() == entity.getSiteId());
                    handler.put("map", map).render();
                }
            }
        } else if (null != surveyId) {
            Long[] userIds = handler.getLongArray("userIds");
            if (CommonUtils.notEmpty(userIds)) {
                CmsUserSurveyId[] entityIds = new CmsUserSurveyId[userIds.length];
                for (int i = 0; i < userIds.length; i++) {
                    entityIds[i] = new CmsUserSurveyId(userIds[i], surveyId);
                }
                List<CmsUserSurvey> entityList = service.getEntitys(entityIds);
                Map<String, CmsUserSurvey> map = CommonUtils.listToMap(entityList, k -> String.valueOf(k.getId().getUserId()),
                        null, entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsUserSurveyService service;

}
