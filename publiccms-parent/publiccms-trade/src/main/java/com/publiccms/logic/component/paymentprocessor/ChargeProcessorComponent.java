package com.publiccms.logic.component.paymentprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.logic.service.trade.TradeAccountHistoryService;
import com.publiccms.logic.service.trade.TradeAccountService;

@Component
public class ChargeProcessorComponent implements TradePaymentProcessor {
    public static final String GRADE_TYPE = "charge";
    @Autowired
    private TradeAccountService accountService;

    @Override
    public String getTradeType() {
        return GRADE_TYPE;
    }

    @Override
    public boolean paid(TradePayment payment) {
        return payment.isProcessed()
                || null != accountService.change(payment.getSiteId(), payment.getAccountSerialNumber(), payment.getUserId(),
                        payment.getUserId(), TradeAccountHistoryService.STATUS_CHARGE, payment.getAmount(), payment.getDescription());
    }

    @Override
    public boolean refunded(TradePayment payment) {
        return payment.isProcessed() || null != accountService.change(payment.getSiteId(), payment.getAccountSerialNumber(),
                payment.getUserId(), payment.getUserId(), TradeAccountHistoryService.STATUS_REFUND, payment.getAmount().negate(),
                payment.getDescription());
    }

    @Override
    public boolean cancel(TradePayment payment) {
        return true;
    }

}
