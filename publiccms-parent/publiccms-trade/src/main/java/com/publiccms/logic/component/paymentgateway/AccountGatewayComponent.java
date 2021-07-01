package com.publiccms.logic.component.paymentgateway;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractPaymentGateway;
import com.publiccms.entities.trade.TradeAccountHistory;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.service.trade.TradeAccountHistoryService;
import com.publiccms.logic.service.trade.TradeAccountService;
import com.publiccms.logic.service.trade.TradePaymentService;

@Component
public class AccountGatewayComponent extends AbstractPaymentGateway {
    /**
     * 
     */
    @Autowired
    private TradePaymentService service;
    @Autowired
    private TradeAccountService accountService;

    @Override
    public String getAccountType() {
        return "account";
    }

    @Override
    public boolean pay(short siteId, TradePayment payment, String callbackUrl, HttpServletResponse response) {
        if (null != payment && payment.getStatus() == TradePaymentService.STATUS_PENDING_PAY) {
            TradeAccountHistory history = accountService.change(siteId, payment.getSerialNumber(), payment.getUserId(),
                    payment.getUserId(), TradeAccountHistoryService.STATUS_PAY, payment.getAmount().negate(),
                    payment.getDescription());
            if (null != history) {
                if (service.paid(siteId, payment.getId(), history.getId().toString())) {
                    if (confirmPay(siteId, payment, response)) {
                        try {
                            response.sendRedirect(callbackUrl);
                        } catch (IOException e) {
                        }
                    }
                    return true;
                } else {
                    accountService.change(siteId, payment.getSerialNumber(), payment.getUserId(), payment.getUserId(),
                            TradeAccountHistoryService.STATUS_REFUND, payment.getAmount(), payment.getDescription());
                }
            }
        }
        return false;
    }

    @Override
    public boolean refund(short siteId, TradePayment payment, TradeRefund refund) {
        if (null != payment && service.refunded(siteId, payment.getId())) {
            TradeAccountHistory history = accountService.change(siteId, payment.getSerialNumber(), payment.getUserId(),
                    payment.getUserId(), TradeAccountHistoryService.STATUS_REFUND, payment.getAmount().negate(),
                    payment.getDescription());
            if (null == history) {
                service.pendingRefund(siteId, payment.getId());
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean enable(short siteId) {
        return true;
    }
}
