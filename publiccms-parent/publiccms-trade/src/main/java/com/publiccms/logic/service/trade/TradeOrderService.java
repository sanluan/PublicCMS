package com.publiccms.logic.service.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

// Generated 2021-6-26 20:16:25 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
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
import com.publiccms.logic.service.cms.CmsContentProductService;

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
    public static final int STATUS_INVALID = 1;
    /**
     * 
     */
    public static final int STATUS_PAID = 2;
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
     * @param status
     * @param processed
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, Long paymentId, Integer[] status, Boolean processed,
            Date startCreateDate, Date endCreateDate, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, paymentId, status, processed, startCreateDate, endCreateDate, orderType, pageIndex,
                pageSize);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Long create(short siteId, long userId, TradeOrder entity, String ip, List<TradeOrderProduct> tradeOrderProductList) {
        if (null != entity) {
            entity.setId(null);
            entity.setSiteId(siteId);
            entity.setUserId(userId);
            entity.setAmount(BigDecimal.ZERO);
            entity.setStatus(STATUS_PENDING);
            entity.setConfirmed(false);
            entity.setProcessed(false);
            entity.setProcessInfo(null);
            entity.setPaymentDate(null);
            entity.setPaymentId(null);
            entity.setProcessUserId(null);
            entity.setCreateDate(null);
            entity.setUpdateDate(null);
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
        if (null != entity && siteId == entity.getSiteId() && !entity.isConfirmed()
                && (entity.getStatus() == STATUS_PENDING || entity.getStatus() == STATUS_PAID)) {
            Date now = CommonUtils.getDate();
            entity.setConfirmed(true);
            entity.setUpdateDate(now);
            productService.deduction(siteId, tradeOrderProductService.getList(siteId, orderId));
            TradeOrderHistory history = new TradeOrderHistory(siteId, orderId, now, TradeOrderHistoryService.OPERATE_CONFIRM);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean invalid(short siteId, long orderId) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && siteId == entity.getSiteId()
                && (entity.getStatus() == STATUS_PENDING || entity.getStatus() == STATUS_PAID)) {
            entity.setStatus(STATUS_INVALID);
            Date now = CommonUtils.getDate();
            entity.setUpdateDate(now);
            TradeOrderHistory history = new TradeOrderHistory(siteId, orderId, now, TradeOrderHistoryService.OPERATE_INVALID);
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
    public boolean processed(short siteId, long orderId, long userId, String processInfo) {
        TradeOrder entity = getEntity(orderId);
        if (null != entity && siteId == entity.getSiteId() && entity.isConfirmed() && !entity.isProcessed()) {
            entity.setProcessed(true);
            entity.setProcessUserId(userId);
            entity.setProcessInfo(processInfo);
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
        if (null != entity && siteId == entity.getSiteId()
                && (entity.getStatus() == STATUS_PENDING || entity.getStatus() == STATUS_INVALID)) {
            entity.setStatus(STATUS_CLOSE);
            Date now = CommonUtils.getDate();
            entity.setUpdateDate(now);
            TradeOrderHistory history = new TradeOrderHistory(siteId, orderId, now, TradeOrderHistoryService.OPERATE_CLOSE);
            historyDao.save(history);
            return true;
        }
        return false;
    }

    @Resource
    private TradeOrderDao dao;
    @Resource
    private CmsContentProductService productService;
    @Resource
    private TradeOrderProductService tradeOrderProductService;
    @Resource
    private TradeOrderHistoryDao historyDao;
}