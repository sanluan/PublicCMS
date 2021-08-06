package com.publiccms.logic.dao.trade;

// Generated 2019-6-15 20:08:45 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.trade.TradeRefund;

/**
 *
 * TradeRefundDao
 * 
 */
@Repository
public class TradeRefundDao extends BaseDao<TradeRefund> {

    /**
     * 
     * @param siteId
     * @param userId
     * @param paymentId
     * @param refundUserId
     * @param status
     * @param paymentType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, Long paymentId, Long refundUserId, Integer status, String paymentType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from TradeRefund bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(paymentId)) {
            queryHandler.condition("bean.paymentId = :paymentId").setParameter("paymentId", paymentId);
        }
        if (CommonUtils.notEmpty(refundUserId)) {
            queryHandler.condition("bean.refundUserId = :refundUserId").setParameter("refundUserId", refundUserId);
        }
        if (CommonUtils.notEmpty(status)) {
            queryHandler.condition("bean.status = :status").setParameter("status", status);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(paymentType)) {
            paymentType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.createDate").append(paymentType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected TradeRefund init(TradeRefund entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}