package com.publiccms.logic.dao.cms;

// Generated 2021-09-23 16:55:08 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsUserSurveyQuestion;

/**
 *
 * CmsUserSurveyQuestionDao
 * 
 */
@Repository
public class CmsUserSurveyQuestionDao extends BaseDao<CmsUserSurveyQuestion> {

    /**
     * @param siteId
     * @param userId
     * @param questionId
     * @param surveyId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, Long questionId, Long surveyId, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsUserSurveyQuestion bean");
        if (null != userId) {
            queryHandler.condition("bean.id.userId = :userId").setParameter("userId", userId);
        }
        if (null != questionId) {
            queryHandler.condition("bean.id.questionId = :questionId").setParameter("questionId", questionId);
        }
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (null != surveyId) {
            queryHandler.condition("bean.surveyId = :surveyId").setParameter("surveyId", surveyId);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "score":
            queryHandler.order("bean.score").append(orderType);
            break;
        default:
            queryHandler.order("bean.createDate").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsUserSurveyQuestion init(CmsUserSurveyQuestion entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}