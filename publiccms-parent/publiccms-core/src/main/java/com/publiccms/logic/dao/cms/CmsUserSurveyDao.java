package com.publiccms.logic.dao.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsUserSurvey;

/**
 *
 * CmsUserSurveyDao
 * 
 */
@Repository
public class CmsUserSurveyDao extends BaseDao<CmsUserSurvey> {

    /**
     * @param siteId
     * @param userId
     * @param surveyId
     * @param anonymous 
     * @param ip 
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, Long surveyId, Boolean anonymous, String ip, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsUserSurvey bean");
        if (null != userId) {
            queryHandler.condition("bean.id.userId = :userId").setParameter("userId", userId);
        }
        if (null != surveyId) {
            queryHandler.condition("bean.id.surveyId = :surveyId").setParameter("surveyId", surveyId);
        }
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (null != anonymous) {
            queryHandler.condition("bean.anonymous = :anonymous").setParameter("anonymous", anonymous);
        }
        if (CommonUtils.notEmpty(ip)) {
            queryHandler.condition("bean.ip like :ip").setParameter("ip", like(ip));
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = Constants.BLANK;
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
    protected CmsUserSurvey init(CmsUserSurvey entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}