package com.publiccms.logic.service.trade;

import java.math.BigDecimal;

// Generated 2019-6-15 20:08:45 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.dao.trade.TradeRefundDao;

/**
 *
 * TradeRefundService
 * 
 */
@Service
@Transactional
public class TradeRefundService extends BaseService<TradeRefund> {
    /**
     * 
     */
    public static final int STATUS_PENDING = 0;
    /**
     * 
     */
    public static final int STATUS_REFUNDED = 1;
    /**
     * 
     */
    public static final int STATUS_CANCELLED = 2;
    /**
     * 
     */
    public static final int STATUS_REFUSE = 3;
    /**
     * 
     */
    public static final int STATUS_FAIL = 4;

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
    @Transactional(readOnly = true)
    public PageHandler getPage(Long orderId, Long refundUserId, Integer status, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(orderId, refundUserId, status, orderType, pageIndex, pageSize);
    }

    /**
     * @param refundId
     * @param amount
     * @param reason
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean updateAmound(long refundId, BigDecimal amount, String reason) {
        TradeRefund entity = getEntity(refundId);
        if (null != entity && entity.getStatus() == STATUS_PENDING) {
            entity.setAmount(amount);
            entity.setReason(reason);
            entity.setUpdateDate(CommonUtils.getDate());
            return true;
        }
        return false;
    }

    /**
     * @param refundId
     * @param refundAmount
     * @param reply
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean updateResund(long refundId, BigDecimal refundAmount, String reply) {
        TradeRefund entity = getEntity(refundId);
        if (null != entity && entity.getStatus() == STATUS_PENDING) {
            if (null == refundAmount) {
                refundAmount = entity.getAmount();
            }
            entity.setRefundAmount(refundAmount);
            entity.setReply(reply);
            return true;
        }
        return false;
    }

    /**
     * @param refundId
     * @param refundUserId
     * @param status
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean updateStatus(long refundId, Long refundUserId, int status) {
        TradeRefund entity = getEntity(refundId);
        if (null != entity && entity.getStatus() == STATUS_PENDING) {
            entity.setStatus(status);
            if (status == STATUS_REFUNDED || status == STATUS_FAIL) {
                entity.setRefundUserId(refundUserId);
                entity.setProcessingDate(CommonUtils.getDate());
            }
            entity.setUpdateDate(CommonUtils.getDate());
            return true;
        }
        return false;
    }

    @Autowired
    private TradeRefundDao dao;

}