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
     * @param orderId
     * @param refundUserId
     * @param status
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long orderId, Long refundUserId, Integer status, String orderType, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from TradeRefund bean");
        if (CommonUtils.notEmpty(orderId)) {
            queryHandler.condition("bean.orderId = :orderId").setParameter("orderId", orderId);
        }
        if (CommonUtils.notEmpty(refundUserId)) {
            queryHandler.condition("bean.refundUserId = :refundUserId").setParameter("refundUserId", refundUserId);
        }
        if (CommonUtils.notEmpty(status)) {
            queryHandler.condition("bean.status = :status").setParameter("status", status);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.createDate " + orderType);
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