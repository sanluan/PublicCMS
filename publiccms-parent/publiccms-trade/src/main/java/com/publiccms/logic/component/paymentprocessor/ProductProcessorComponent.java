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
    public boolean paid(TradePayment payment) {
        return payment.isProcessed() || orderService.paid(payment.getSiteId(), Long.parseLong(payment.getAccountSerialNumber()));
    }

    @Override
    public boolean refunded(TradePayment payment) {
        return payment.isProcessed()
                || orderService.refunded(payment.getSiteId(), Long.parseLong(payment.getAccountSerialNumber()));
    }

    @Override
    public boolean cancel(TradePayment payment) {
        return payment.isProcessed()
                || orderService.cancelPayment(payment.getSiteId(), Long.parseLong(payment.getAccountSerialNumber()));
    }

}
