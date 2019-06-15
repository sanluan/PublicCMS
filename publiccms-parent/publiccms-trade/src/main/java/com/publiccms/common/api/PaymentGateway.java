package com.publiccms.common.api;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeRefund;

public interface PaymentGateway {
    public String getChannel();

    public boolean enable(short siteId);

    public void pay(TradeOrder order, String callbackUrl, String notifyUrl, HttpServletResponse response);

    public boolean refund(TradeOrder order, TradeRefund refund);

    public String notify(short siteId, String body, Map<String, String[]> parameterMap);
}
