package com.publiccms.logic.component.paymentgateway;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.api.TradeOrderProcessor;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradeAccountHistory;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeOrderHistory;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.trade.TradeOrderProcessorComponent;
import com.publiccms.logic.service.trade.TradeAccountHistoryService;
import com.publiccms.logic.service.trade.TradeAccountService;
import com.publiccms.logic.service.trade.TradeOrderHistoryService;
import com.publiccms.logic.service.trade.TradeOrderService;

@Component
public class AccountGatewayComponent implements PaymentGateway {
    /**
     * 
     */
    @Autowired
    private TradeOrderService service;
    @Autowired
    private TradeOrderHistoryService historyService;
    @Autowired
    private TradeAccountService accountService;
    @Autowired
    private TradeOrderProcessorComponent tradeOrderProcessorComponent;

    @Override
    public String getAccountType() {
        return "account";
    }

    @Override
    public boolean pay(SysSite site, TradeOrder order, String callbackUrl, HttpServletResponse response) {
        if (null != order && order.getStatus() == TradeOrderService.STATUS_PENDING_PAY
                && null != accountService.change(order.getSiteId(), order.getAccountSerialNumber(), order.getUserId(),
                        order.getUserId(), TradeAccountHistoryService.STATUS_PAY, order.getAmount().negate(),
                        order.getDescription())) {
            if (service.paid(order.getSiteId(), order.getId(), order.getAccountSerialNumber())) {
                TradeOrderProcessor tradeOrderProcessor = tradeOrderProcessorComponent.get(order.getTradeType());
                if (null != tradeOrderProcessor && tradeOrderProcessor.paid(order)) {
                    service.processed(order.getSiteId(), order.getId());
                    return true;
                } else {
                    TradeOrderHistory history = new TradeOrderHistory(order.getSiteId(), order.getId(), CommonUtils.getDate(),
                            TradeOrderHistoryService.OPERATE_PROCESS_ERROR);
                    historyService.save(history);
                }
            } else {
                accountService.change(order.getSiteId(), order.getAccountSerialNumber(), order.getUserId(), order.getUserId(),
                        TradeAccountHistoryService.STATUS_REFUND, order.getAmount(), order.getDescription());
            }
        }
        try {
            response.sendRedirect(callbackUrl);
        } catch (IOException e) {
        }
        return false;
    }

    @Override
    public boolean refund(SysSite site, TradeOrder order, TradeRefund refund) {
        if (null != order && service.refunded(order.getSiteId(), order.getId())) {
            TradeAccountHistory history = accountService.change(order.getSiteId(), order.getAccountSerialNumber(),
                    order.getUserId(), order.getUserId(), TradeAccountHistoryService.STATUS_REFUND, order.getAmount().negate(),
                    order.getDescription());
            if (null == history) {
                service.pendingRefund(order.getSiteId(), order.getId());
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
