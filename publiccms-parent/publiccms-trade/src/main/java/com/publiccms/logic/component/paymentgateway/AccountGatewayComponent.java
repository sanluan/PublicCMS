package com.publiccms.logic.component.paymentgateway;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractPaymentGateway;
import com.publiccms.entities.sys.SysSite;
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
    @Resource
    private TradePaymentService service;
    @Resource
    private TradeAccountService accountService;

    @Override
    public String getAccountType() {
        return "account";
    }

    @Override
    public boolean pay(SysSite site, TradePayment payment, String paymentType, String callbackUrl, HttpServletResponse response) {
        if (null != payment && payment.getStatus() == TradePaymentService.STATUS_PENDING_PAY) {
            TradeAccountHistory history = accountService.change(site.getId(), payment.getSerialNumber(), payment.getUserId(),
                    payment.getUserId(), TradeAccountHistoryService.STATUS_PAY, payment.getAmount().negate(),
                    payment.getDescription());
            if (null != history) {
                if (service.paid(site.getId(), payment.getId(), history.getId().toString())) {
                    if (confirmPay(site.getId(), payment, response)) {
                        try {
                            response.sendRedirect(callbackUrl);
                        } catch (IOException e) {
                        }
                    }
                    return true;
                } else {
                    accountService.change(site.getId(), payment.getSerialNumber(), payment.getUserId(), payment.getUserId(),
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
