package com.publiccms.logic.service.trade;

// Generated 2019-6-15 18:52:24 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.logic.dao.trade.TradeOrderDao;

/**
 *
 * TradeOrderService
 * 
 */
@Service
@Transactional
public class TradeOrderService extends BaseService<TradeOrder> {
    /**
     * 
     */
    public static final int STATUS_PENDING_PAY = 0;
    /**
     * 
     */
    public static final int STATUS_PAID = 1;
    /**
     * 
     */
    public static final int STATUS_PENDING_REFUND = 2;
    /**
     * 
     */
    public static final int STATUS_REFUNDED = 3;

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
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, String tradeType, String serialNumber, String accountType,
            String accountSerialNumber, Integer status, Date startCreateDate, Date endCreateDate, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, tradeType, serialNumber, accountType, accountSerialNumber, status, startCreateDate,
                endCreateDate, orderType, pageIndex, pageSize);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean paid(long orderId, String accountSerialNumber) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && entity.getStatus() == STATUS_PENDING_PAY) {
            entity.setStatus(STATUS_PAID);
            entity.setAccountSerialNumber(accountSerialNumber);
            entity.setPaymentDate(CommonUtils.getDate());
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean pendingRefund(long orderId) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && (entity.getStatus() == STATUS_PAID || entity.getStatus() == STATUS_REFUNDED)) {
            entity.setStatus(STATUS_PENDING_REFUND);
            entity.setUpdateDate(CommonUtils.getDate());
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean refunded(long orderId) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && (entity.getStatus() == STATUS_PENDING_REFUND)) {
            entity.setStatus(STATUS_REFUNDED);
            entity.setUpdateDate(CommonUtils.getDate());
            return true;
        }
        return false;
    }

    @Autowired
    private TradeOrderDao dao;

}