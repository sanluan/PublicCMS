package com.publiccms.logic.dao.cms;

import java.util.Date;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsSurvey;

/**
 *
 * CmsSurveyDao
 * 
 */
@Repository
public class CmsSurveyDao extends BaseDao<CmsSurvey> {

    /**
     * @param siteId
     * @param userId
     * @param surveyType
     * @param startStartDate
     * @param endStartDate
     * @param startEndDate
     * @param endEndDate
     * @param title
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, String surveyType, Date startStartDate, Date endStartDate,
            Date startEndDate, Date endEndDate, String title, Boolean disabled, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsSurvey bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (null != startStartDate) {
            queryHandler.condition("bean.startDate > :startStartDate").setParameter("startStartDate", startStartDate);
        }
        if (null != endStartDate) {
            queryHandler.condition("bean.startDate <= :endStartDate").setParameter("endStartDate", endStartDate);
        }
        if (null != startEndDate) {
            queryHandler.condition("bean.endDate > :startEndDate").setParameter("startEndDate", startEndDate);
        }
        if (null != endEndDate) {
            queryHandler.condition("bean.endDate <= :endEndDate").setParameter("endEndDate", endEndDate);
        }
        if (CommonUtils.notEmpty(title)) {
            queryHandler.condition("bean.title like :title").setParameter("title", like(title));
        }
        if (null != disabled) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "votes":
            queryHandler.order("bean.votes").append(orderType);
            break;
        case "startDate":
            queryHandler.order("bean.startDate").append(orderType);
            break;
        case "endDate":
            queryHandler.order("bean.endDate").append(orderType);
            break;
        case "createDate":
            queryHandler.order("bean.createTime").append(orderType);
            break;
        default:
            queryHandler.order("bean.id").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsSurvey init(CmsSurvey entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        if (CommonUtils.notEmpty(entity.getTitle()) && entity.getTitle().length() > 255) {
            entity.setTitle(entity.getTitle().substring(0, 255));
        }
        if (CommonUtils.notEmpty(entity.getDescription()) && entity.getDescription().length() > 300) {
            entity.setDescription(entity.getDescription().substring(0, 300));
        }
        return entity;
    }

}