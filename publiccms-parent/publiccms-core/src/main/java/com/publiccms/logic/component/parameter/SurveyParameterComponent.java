package com.publiccms.logic.component.parameter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractLongParameterHandler;
import com.publiccms.entities.cms.CmsSurvey;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsSurveyService;

import jakarta.annotation.Priority;
import jakarta.annotation.Resource;

/**
 * SurveyParameterComponent 问卷参数处理组件
 */
@Component
@Priority(10)
public class SurveyParameterComponent extends AbstractLongParameterHandler<CmsSurvey> {
    @Resource
    private CmsSurveyService service;

    @Override
    public String getType() {
        return Config.INPUTTYPE_SURVEY;
    }

    @Override
    public List<CmsSurvey> getParameterValueList(SysSite site, Long[] ids) {
        List<CmsSurvey> entityList = service.getEntitys(ids);
        return entityList.stream().filter(entity -> site.getId() == entity.getSiteId() && !entity.isDisabled())
                .toList();
    }

    @Override
    public CmsSurvey getParameterValue(SysSite site, Long id) {
        CmsSurvey entity = service.getEntity(id);
        if (null != entity && !entity.isDisabled() && entity.getSiteId() == site.getId()) {
            return entity;
        }
        return null;
    }
}
