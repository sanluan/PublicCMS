package com.publiccms.common.base;

import jakarta.servlet.http.HttpServletResponse;

import jakarta.annotation.Resource;

import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradePaymentHistory;
import com.publiccms.logic.component.trade.PaymentProcessorComponent;
import com.publiccms.logic.service.trade.TradePaymentHistoryService;
import com.publiccms.logic.service.trade.TradePaymentService;

public abstract class AbstractPaymentGateway implements PaymentGateway {
    @Resource
    private TradePaymentService service;
    @Resource
    private TradePaymentHistoryService historyService;
    @Resource
    private PaymentProcessorComponent paymentProcessorComponent;

    @Override
    public boolean confirmPay(short siteId, TradePayment payment, HttpServletResponse response) {
        TradePaymentProcessor paymentProcessor = paymentProcessorComponent.get(payment.getTradeType());
        if (null != paymentProcessor && paymentProcessor.paid(siteId, payment)) {
            service.processed(siteId, payment.getId(), payment.getUserId());
            return true;
        } else {
            TradePaymentHistory history = new TradePaymentHistory(siteId, payment.getId(), CommonUtils.getDate(),
                    TradePaymentHistoryService.OPERATE_PROCESS_ERROR);
            historyService.save(history);
        }
        return false;
    }
}
