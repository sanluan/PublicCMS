package com.publiccms.logic.service.trade;

// Generated 2019-6-16 9:47:27 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.trade.TradePaymentHistory;
import com.publiccms.logic.dao.trade.TradePaymentHistoryDao;

/**
 *
 * TradePaymentHistoryService
 * 
 */
@Service
@Transactional
public class TradePaymentHistoryService extends BaseService<TradePaymentHistory> {
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
    public static final String OPERATE_PAYERROR = "payError";
    /**
     * 
     */
    public static final String OPERATE_PROCESSED = "processed";
    /**
     * 
     */
    public static final String OPERATE_PROCESS_ERROR = "processError";
    /**
     * 
     */
    public static final String OPERATE_NOTIFY = "notify";
    /**
     * 
     */
    public static final String OPERATE_PENDING_REFUND = "pendingRefund";
    /**
     * 
     */
    public static final String OPERATE_REFUND = "refund";
    /**
     * 
     */
    public static final String OPERATE_REFUNDERROR = "refundError";
    /**
     * 
     */
    public static final String OPERATE_REFUND_RESPONSE = "refundResponse";
    /**
     * 
     */
    public static final String OPERATE_CLOSE = "close";

    /**
     * 
     * @param siteId
     * @param paymentId
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long paymentId, Date startCreateDate, Date endCreateDate, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, paymentId, startCreateDate, endCreateDate, orderType, pageIndex, pageSize);
    }

    @Resource
    private TradePaymentHistoryDao dao;

}