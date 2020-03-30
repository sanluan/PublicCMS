package com.publiccms.logic.component.orderprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.TradeOrderProcessor;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.logic.service.trade.TradeAccountHistoryService;
import com.publiccms.logic.service.trade.TradeAccountService;

@Component
public class ChargeProcessorComponent implements TradeOrderProcessor {
    public static final String GRADE_TYPE = "charge";
    @Autowired
    private TradeAccountService accountService;

    @Override
    public String getTradeType() {
        return GRADE_TYPE;
    }

    @Override
    public boolean paid(TradeOrder order) {
        return order.isProcessed()
                || null != accountService.change(order.getSiteId(), order.getAccountSerialNumber(), order.getUserId(),
                        order.getUserId(), TradeAccountHistoryService.STATUS_CHARGE, order.getAmount(), order.getDescription());
    }

    @Override
    public boolean refunded(TradeOrder order) {
        return order.isProcessed() || null != accountService.change(order.getSiteId(), order.getAccountSerialNumber(),
                order.getUserId(), order.getUserId(), TradeAccountHistoryService.STATUS_REFUND, order.getAmount().negate(),
                order.getDescription());
    }

}
