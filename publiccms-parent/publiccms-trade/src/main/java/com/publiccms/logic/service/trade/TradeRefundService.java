package com.publiccms.logic.service.trade;

import java.math.BigDecimal;

// Generated 2019-6-15 20:08:45 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;
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
     * @param siteId
     * @param userId
     * @param paymentId
     * @param refundUserId
     * @param status
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, Long paymentId, Long refundUserId, Integer status, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, paymentId, refundUserId, status, orderType, pageIndex, pageSize);
    }

    /**
     * @param refundId
     * @param userId
     * @param amount
     * @param reason
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean updateAmound(long refundId, long userId, BigDecimal amount, String reason) {
        TradeRefund entity = getEntity(refundId);
        if (null != entity && entity.getUserId() == userId && entity.getStatus() == STATUS_PENDING
                || entity.getStatus() == STATUS_REFUSE) {
            entity.setAmount(amount);
            entity.setReason(reason);
            entity.setStatus(STATUS_PENDING);
            entity.setUpdateDate(CommonUtils.getDate());
            return true;
        }
        return false;
    }

    /**
     * @param siteId
     * @param refundId
     * @param refundUserId
     * @param reply
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean refuseResund(short siteId, long refundId, Long refundUserId, String reply) {
        TradeRefund entity = getEntity(refundId);
        if (null != entity && entity.getSiteId() == siteId
                && (entity.getStatus() == STATUS_PENDING || entity.getStatus() == STATUS_FAIL)) {
            entity.setRefundUserId(refundUserId);
            entity.setStatus(STATUS_REFUSE);
            entity.setReply(reply);
            return true;
        }
        return false;
    }

    /**
     * @param siteId
     * @param refundId
     * @param refundAmount
     * @param reply
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean updateResund(short siteId, long refundId, BigDecimal refundAmount, String reply) {
        TradeRefund entity = getEntity(refundId);
        if (null != entity && entity.getSiteId() == siteId
                && (entity.getStatus() == STATUS_PENDING || entity.getStatus() == STATUS_FAIL)) {
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
     * @param siteId
     * @param refundId
     * @param refundUserId
     * @param userId
     * @param status
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean updateStatus(short siteId, long refundId, Long refundUserId, Long userId, int status) {
        TradeRefund entity = getEntity(refundId);
        if (null != entity && entity.getSiteId() == siteId && entity.getStatus() != status
                && (null == userId || entity.getUserId() == userId) && entity.getStatus() != STATUS_CANCELLED
                && entity.getStatus() != STATUS_REFUNDED) {
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

    @Resource
    private TradeRefundDao dao;

}