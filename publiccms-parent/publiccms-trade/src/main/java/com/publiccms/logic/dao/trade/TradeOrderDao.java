package com.publiccms.logic.dao.trade;

import java.util.Date;

// Generated 2021-6-26 20:16:25 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.trade.TradeOrder;

/**
 *
 * TradeOrderDao
 * 
 */
@Repository
public class TradeOrderDao extends BaseDao<TradeOrder> {

    /**
     * @param siteId
     * @param userId
     * @param paymentId
     * @param status
     * @param processed
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, Long paymentId, Integer[] status, Boolean processed,
            Date startCreateDate, Date endCreateDate, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from TradeOrder bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(paymentId)) {
            queryHandler.condition("bean.paymentId = :paymentId").setParameter("paymentId", paymentId);
        }
        if (CommonUtils.notEmpty(status)) {
            queryHandler.condition("bean.status in (:status)").setParameter("status", status);
        }
        if (null != processed) {
            queryHandler.condition("bean.processed = :processed").setParameter("processed", processed);
        }
        if (null != startCreateDate) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (null != endCreateDate) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.createDate").append(orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected TradeOrder init(TradeOrder entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}