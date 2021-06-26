package com.publiccms.logic.service.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

// Generated 2021-6-26 20:16:25 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeOrderHistory;
import com.publiccms.entities.trade.TradeOrderProduct;
import com.publiccms.logic.dao.trade.TradeOrderDao;
import com.publiccms.logic.dao.trade.TradeOrderHistoryDao;

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
    public static final int STATUS_PENDING = 0;
    /**
     * 
     */
    public static final int STATUS_CONFIRMED = 1;
    /**
     * 
     */
    public static final int STATUS_INVALID = 2;
    /**
     * 
     */
    public static final int STATUS_PAID = 3;
    /**
     * 
     */
    public static final int STATUS_REFUNDED = 3;
    /**
     * 
     */
    public static final int STATUS_CLOSE = 4;

    /**
     * @param siteId
     * @param userId
     * @param paymentId
     * @param processed
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, Long paymentId, Boolean processed, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, userId, paymentId, processed, orderType, pageIndex, pageSize);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Long create(short siteId, long userId, TradeOrder entity, String ip, List<TradeOrderProduct> tradeOrderProductList) {
        if (null != entity) {
            entity.setId(null);
            entity.setSiteId(siteId);
            entity.setUserId(userId);
            entity.setCreateDate(null);
            entity.setProcessed(false);
            entity.setProcessInfo(null);
            entity.setAmount(BigDecimal.ZERO);
            entity.setStatus(STATUS_PENDING);
            entity.setIp(ip);
            save(entity);
            TradeOrderHistory history = new TradeOrderHistory(entity.getSiteId(), entity.getId(), CommonUtils.getDate(),
                    TradeOrderHistoryService.OPERATE_CREATE);
            historyDao.save(history);
            BigDecimal amount = tradeOrderProductService.create(siteId, entity.getId(), tradeOrderProductList);
            entity = getEntity(entity.getId());
            if (null != amount) {
                entity.setAmount(amount);
            } else {
                entity.setStatus(STATUS_INVALID);
            }
            return entity.getId();
        }
        return null;

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean confirm(short siteId, long orderId) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setStatus(STATUS_CONFIRMED);
            Date now = CommonUtils.getDate();
            entity.setUpdateDate(now);
            TradeOrderHistory history = new TradeOrderHistory(siteId, orderId, now, TradeOrderHistoryService.OPERATE_PROCESSED);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean pay(short siteId, long orderId, long paymentId) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setPaymentId(paymentId);
            Date now = CommonUtils.getDate();
            entity.setUpdateDate(now);
            TradeOrderHistory history = new TradeOrderHistory(siteId, orderId, now, TradeOrderHistoryService.OPERATE_PAY);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean cancelPayment(short siteId, long orderId) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setPaymentId(null);
            Date now = CommonUtils.getDate();
            entity.setUpdateDate(now);
            TradeOrderHistory history = new TradeOrderHistory(siteId, orderId, now, TradeOrderHistoryService.OPERATE_CANCELPAY);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean processed(short siteId, long orderId) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && siteId == entity.getSiteId() && !entity.isProcessed()) {
            entity.setProcessed(true);
            Date now = CommonUtils.getDate();
            entity.setProcessDate(now);
            entity.setUpdateDate(now);
            TradeOrderHistory history = new TradeOrderHistory(siteId, orderId, now, TradeOrderHistoryService.OPERATE_PROCESSED);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean paid(short siteId, long orderId) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && siteId == entity.getSiteId() && entity.getStatus() == STATUS_PENDING) {
            entity.setStatus(STATUS_PAID);
            Date now = CommonUtils.getDate();
            entity.setPaymentDate(now);
            entity.setUpdateDate(now);
            TradeOrderHistory history = new TradeOrderHistory(siteId, orderId, now, TradeOrderHistoryService.OPERATE_PAID);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean refunded(short siteId, long orderId) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && siteId == entity.getSiteId() && (entity.getStatus() == STATUS_PAID)) {
            entity.setStatus(STATUS_REFUNDED);
            Date now = CommonUtils.getDate();
            entity.setUpdateDate(now);
            TradeOrderHistory history = new TradeOrderHistory(siteId, orderId, now, TradeOrderHistoryService.OPERATE_REFUND);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean close(short siteId, long orderId) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && siteId == entity.getSiteId() && (entity.getStatus() == STATUS_PENDING
                || entity.getStatus() == STATUS_INVALID || entity.getStatus() == STATUS_REFUNDED)) {
            entity.setStatus(STATUS_CLOSE);
            Date now = CommonUtils.getDate();
            entity.setUpdateDate(now);
            TradeOrderHistory history = new TradeOrderHistory(siteId, orderId, now, TradeOrderHistoryService.OPERATE_CLOSE);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Autowired
    private TradeOrderDao dao;
    @Autowired
    private TradeOrderProductService tradeOrderProductService;
    @Autowired
    private TradeOrderHistoryDao historyDao;
}