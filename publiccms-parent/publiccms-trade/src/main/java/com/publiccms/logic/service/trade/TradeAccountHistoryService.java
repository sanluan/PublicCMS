package com.publiccms.logic.service.trade;

// Generated 2019-6-16 9:47:27 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.trade.TradeAccountHistory;
import com.publiccms.logic.dao.trade.TradeAccountHistoryDao;

/**
 *
 * TradeAccountHistoryService
 * 
 */
@Service
@Transactional
public class TradeAccountHistoryService extends BaseService<TradeAccountHistory> {
    /**
     * 
     */
    public static final int STATUS_PEND = 0;
    /**
     * 
     */
    public static final int STATUS_PAY = 1;
    /**
     * 
     */
    public static final int STATUS_CHARGE = 2;
    /**
     * 
     */
    public static final int STATUS_REFUND = 3;

    /**
     * 
     * @param siteId
     * @param accountId
     * @param userId
     * @param status
     * @param startCreateDate
     * @param endCreateDate
     * @param paymentType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional
    public PageHandler getPage(Short siteId, Long accountId, Long userId, Integer status, Date startCreateDate,
            Date endCreateDate, String paymentType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, accountId, userId, status, startCreateDate, endCreateDate, paymentType, pageIndex, pageSize);
    }

    @Resource
    private TradeAccountHistoryDao dao;

}