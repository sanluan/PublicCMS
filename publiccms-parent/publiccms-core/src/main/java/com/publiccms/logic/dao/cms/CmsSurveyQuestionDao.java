package com.publiccms.logic.dao.cms;

// Generated 2021-09-23 16:55:08 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsSurveyQuestion;

/**
 *
 * CmsSurveyQuestionDao
 * 
 */
@Repository
public class CmsSurveyQuestionDao extends BaseDao<CmsSurveyQuestion> {

    /**
     * @param surveyId
     * @param inputTypes
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long surveyId, String[] inputTypes, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsSurveyQuestion bean");
        if (CommonUtils.notEmpty(surveyId)) {
            queryHandler.condition("bean.surveyId = :surveyId").setParameter("surveyId", surveyId);
        }
        if (CommonUtils.notEmpty(inputTypes)) {
            queryHandler.condition("bean.inputTypes in (:inputTypes)").setParameter("inputTypes", inputTypes);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.sort " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsSurveyQuestion init(CmsSurveyQuestion entity) {
        if (CommonUtils.notEmpty(entity.getTitle()) && entity.getTitle().length() > 255) {
            entity.setTitle(entity.getTitle().substring(0, 255));
        }
        return entity;
    }

}