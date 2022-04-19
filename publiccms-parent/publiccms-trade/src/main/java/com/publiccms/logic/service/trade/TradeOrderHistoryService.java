package com.publiccms.logic.service.trade;

// Generated 2019-6-16 9:47:27 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.trade.TradeOrderHistory;
import com.publiccms.logic.dao.trade.TradeOrderHistoryDao;

/**
 *
 * TradeOrderHistoryService
 * 
 */
@Service
@Transactional
public class TradeOrderHistoryService extends BaseService<TradeOrderHistory> {
    /**
     * 
     */
    public static final String OPERATE_CREATE = "create";
    /**
     * 
     */
    public static final String OPERATE_PAY = "pay";
    /**
     * 
     */
    public static final String OPERATE_PAID = "paid";
    /**
     * 
     */
    public static final String OPERATE_PROCESSED = "processed";
    /**
     * 
     */
    public static final String OPERATE_INVALID = "invalid";
    /**
     * 
     */
    public static final String OPERATE_CONFIRM = "confirm";
    /**
     * 
     */
    public static final String OPERATE_CANCELPAY = "cancelPay";
    /**
     * 
     */
    public static final String OPERATE_REFUND = "refund";
    /**
     * 
     */
    public static final String OPERATE_CLOSE = "close";

    /**
     * 
     * @param siteId
     * @param orderId
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long orderId, Date startCreateDate, Date endCreateDate, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, orderId, startCreateDate, endCreateDate, orderType, pageIndex, pageSize);
    }

    @Resource
    private TradeOrderHistoryDao dao;

}