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
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradePaymentHistory;
import com.publiccms.logic.dao.trade.TradePaymentDao;
import com.publiccms.logic.dao.trade.TradePaymentHistoryDao;

/**
 *
 * TradePaymentService
 * 
 */
@Service
@Transactional
public class TradePaymentService extends BaseService<TradePayment> {
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
     */
    public static final int STATUS_CLOSE = 4;

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
     * @param paymentType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, String tradeType, String serialNumber, String accountType,
            String accountSerialNumber, Integer[] status, Date startCreateDate, Date endCreateDate, String paymentType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, tradeType, serialNumber, accountType, accountSerialNumber, status, startCreateDate,
                endCreateDate, paymentType, pageIndex, pageSize);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean create(short siteId, TradePayment entity) {
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setStatus(STATUS_PENDING_PAY);
            save(entity);
            TradePaymentHistory history = new TradePaymentHistory(entity.getSiteId(), entity.getId(), CommonUtils.getDate(),
                    TradePaymentHistoryService.OPERATE_CREATE);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean processed(short siteId, long paymentId, long userId) {
        TradePayment entity = getEntity(paymentId);
        if (null != entity && siteId == entity.getSiteId() && !entity.isProcessed()) {
            entity.setProcessed(true);
            entity.setProcessUserId(userId);
            Date now = CommonUtils.getDate();
            entity.setProcessDate(now);
            entity.setUpdateDate(now);
            TradePaymentHistory history = new TradePaymentHistory(siteId, paymentId, now,
                    TradePaymentHistoryService.OPERATE_PROCESSED);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean paid(short siteId, long paymentId, String accountSerialNumber) {
        TradePayment entity = getEntity(paymentId);
        if (null != entity && siteId == entity.getSiteId() && entity.getStatus() == STATUS_PENDING_PAY) {
            entity.setStatus(STATUS_PAID);
            entity.setAccountSerialNumber(accountSerialNumber);
            Date now = CommonUtils.getDate();
            entity.setPaymentDate(now);
            entity.setUpdateDate(now);
            TradePaymentHistory history = new TradePaymentHistory(siteId, paymentId, now, TradePaymentHistoryService.OPERATE_PAY);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean cancel(short siteId, long paymentId) {
        TradePayment entity = getEntity(paymentId);
        if (null != entity && siteId == entity.getSiteId() && entity.getStatus() == STATUS_PENDING_PAY) {
            entity.setStatus(STATUS_CLOSE);
            Date now = CommonUtils.getDate();
            entity.setPaymentDate(now);
            entity.setUpdateDate(now);
            TradePaymentHistory history = new TradePaymentHistory(siteId, paymentId, now, TradePaymentHistoryService.OPERATE_PAY);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean pendingRefund(short siteId, long paymentId) {
        TradePayment entity = getEntity(paymentId);
        if (null != entity && siteId == entity.getSiteId()
                && (entity.getStatus() == STATUS_PAID || entity.getStatus() == STATUS_REFUNDED)) {
            entity.setStatus(STATUS_PENDING_REFUND);
            Date now = CommonUtils.getDate();
            entity.setUpdateDate(now);
            TradePaymentHistory history = new TradePaymentHistory(siteId, paymentId, now,
                    TradePaymentHistoryService.OPERATE_PENDING_REFUND);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean refunded(short siteId, long paymentId) {
        TradePayment entity = getEntity(paymentId);
        if (null != entity && siteId == entity.getSiteId() && (entity.getStatus() == STATUS_PENDING_REFUND)) {
            entity.setStatus(STATUS_REFUNDED);
            Date now = CommonUtils.getDate();
            entity.setUpdateDate(now);
            TradePaymentHistory history = new TradePaymentHistory(siteId, paymentId, now,
                    TradePaymentHistoryService.OPERATE_REFUND);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean close(short siteId, long paymentId) {
        TradePayment entity = getEntity(paymentId);
        if (null != entity && siteId == entity.getSiteId() && (entity.getStatus() == STATUS_REFUNDED)) {
            entity.setStatus(STATUS_CLOSE);
            Date now = CommonUtils.getDate();
            entity.setUpdateDate(now);
            TradePaymentHistory history = new TradePaymentHistory(siteId, paymentId, now,
                    TradePaymentHistoryService.OPERATE_CLOSE);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Autowired
    private TradePaymentDao dao;
    @Autowired
    private TradePaymentHistoryDao historyDao;

}