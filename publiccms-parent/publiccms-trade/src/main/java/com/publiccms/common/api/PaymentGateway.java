package com.publiccms.common.api;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletResponse;

import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradeRefund;

public interface PaymentGateway extends Container<String> {
    String getAccountType();

    default Supplier<String> keyFunction() {
        return () -> getAccountType();
    }

    boolean enable(short siteId);

    boolean pay(short siteId, TradePayment payment, String callbackUrl, HttpServletResponse response);

    boolean confirmPay(short siteId, TradePayment payment, HttpServletResponse response);

    boolean refund(short siteId, TradePayment payment, TradeRefund refund);
}
