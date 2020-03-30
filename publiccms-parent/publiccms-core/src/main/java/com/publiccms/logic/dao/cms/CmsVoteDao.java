package com.publiccms.logic.dao.cms;

// Generated 2020-3-26 12:04:22 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsVote;

/**
 *
 * CmsVoteDao
 * 
 */
@Repository
public class CmsVoteDao extends BaseDao<CmsVote> {

    /**
     * @param siteId
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
    public PageHandler getPage(Short siteId, Date startStartDate, Date endStartDate, Date startEndDate, Date endEndDate,
            String title, Boolean disabled, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsVote bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
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
        case "startDate":
            queryHandler.order("bean.startDate ").append(orderType);
            break;
        case "endDate":
            queryHandler.order("bean.endDate ").append(orderType);
            break;
        case "scores":
            queryHandler.order("bean.scores ").append(orderType);
            break;
        case "createDate":
            queryHandler.order("bean.createDate ").append(orderType);
            break;
        default:
            queryHandler.order("bean.id ").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsVote init(CmsVote entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}