package com.publiccms.logic.component.paymentprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.logic.service.trade.TradeOrderService;

@Component
public class ProductProcessorComponent implements TradePaymentProcessor {
    public static final String GRADE_TYPE = "product";
    @Autowired
    private TradeOrderService orderService;

    @Override
    public String getTradeType() {
        return GRADE_TYPE;
    }

    @Override
    public boolean paid(short siteId, TradePayment payment) {
        return orderService.paid(siteId, Long.parseLong(payment.getSerialNumber()));
    }

    @Override
    public boolean refunded(short siteId, TradePayment payment) {
        return orderService.refunded(siteId, Long.parseLong(payment.getSerialNumber()));
    }

    @Override
    public boolean cancel(short siteId, TradePayment payment) {
        return orderService.cancelPayment(siteId, Long.parseLong(payment.getSerialNumber()));
    }

}
