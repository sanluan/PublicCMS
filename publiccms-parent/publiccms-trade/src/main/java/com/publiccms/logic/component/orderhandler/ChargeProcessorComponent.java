package com.publiccms.logic.component.orderhandler;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.api.TradeOrderProcessor;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.logic.service.trade.TradeAccountHistoryService;
import com.publiccms.logic.service.trade.TradeAccountService;

public class ChargeProcessorComponent implements TradeOrderProcessor {
    public static final String GRADE_TYPE = "charge";
    @Autowired
    private TradeAccountService accountService;

    @Override
    public String getTradeType() {
        return GRADE_TYPE;
    }

    @Override
    public boolean process(TradeOrder order) {
        return order.isProcessed() || null != accountService.change(order.getSiteId(), order.getAccountSerialNumber(),
                order.getUserId(), order.getUserId(), TradeAccountHistoryService.STATUS_CHARGE, order.getAmount());
    }

}
