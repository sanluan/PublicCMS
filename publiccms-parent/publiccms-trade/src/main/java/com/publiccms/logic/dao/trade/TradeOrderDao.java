package com.publiccms.logic.dao.trade;

// Generated 2019-6-15 18:52:24 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;

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
     * 
     * @param siteId
     * @param userId
     * @param tradeType
     * @param serialNumber
     * @param accountType
     * @param accountSerialNumber
     * @param status
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, String tradeType, String serialNumber, String accountType,
            String accountSerialNumber, Integer status, Date startCreateDate, Date endCreateDate, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from TradeOrder bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(tradeType)) {
            queryHandler.condition("bean.tradeType = :tradeType").setParameter("tradeType", tradeType);
        }
        if (CommonUtils.notEmpty(serialNumber)) {
            queryHandler.condition("bean.serialNumber = :serialNumber").setParameter("serialNumber", serialNumber);
        }
        if (CommonUtils.notEmpty(accountType)) {
            queryHandler.condition("bean.accountType = :accountType").setParameter("accountType", accountType);
        }
        if (CommonUtils.notEmpty(accountSerialNumber)) {
            queryHandler.condition("bean.accountSerialNumber = :accountSerialNumber").setParameter("accountSerialNumber",
                    accountSerialNumber);
        }
        if (CommonUtils.notEmpty(status)) {
            queryHandler.condition("bean.status = :status").setParameter("status", status);
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
        queryHandler.order("bean.createDate " + orderType);
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