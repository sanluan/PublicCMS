package com.publiccms.common.base;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.api.TradeOrderProcessor;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeOrderHistory;
import com.publiccms.logic.component.trade.TradeOrderProcessorComponent;
import com.publiccms.logic.service.trade.TradeOrderHistoryService;
import com.publiccms.logic.service.trade.TradeOrderService;

public abstract class AbstractPaymentGateway implements PaymentGateway {
    @Autowired
    private TradeOrderService service;
    @Autowired
    private TradeOrderHistoryService historyService;
    @Autowired
    private TradeOrderProcessorComponent tradeOrderProcessorComponent;

    @Override
    public boolean confirmPay(SysSite site, TradeOrder order, String callbackUrl, HttpServletResponse response) {
        TradeOrderProcessor tradeOrderProcessor = tradeOrderProcessorComponent.get(order.getTradeType());
        if (null != tradeOrderProcessor && tradeOrderProcessor.paid(order)) {
            service.processed(site.getId(), order.getId());
            return true;
        } else {
            TradeOrderHistory history = new TradeOrderHistory(order.getSiteId(), order.getId(), CommonUtils.getDate(),
                    TradeOrderHistoryService.OPERATE_PROCESS_ERROR);
            historyService.save(history);
        }
        if (null != callbackUrl) {
            try {
                response.sendRedirect(callbackUrl);
            } catch (IOException e) {
            }
        }
        return false;
    }
}
