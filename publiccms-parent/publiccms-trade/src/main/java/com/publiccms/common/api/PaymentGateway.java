package com.publiccms.common.api;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletResponse;

import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradeRefund;

public interface PaymentGateway extends Container<String> {
    String getAccountType();

    default Supplier<String> keyFunction() {
        return () -> getAccountType();
    }

    boolean enable(short siteId);

    boolean pay(SysSite site, TradePayment payment, String callbackUrl, HttpServletResponse response);

    boolean confirmPay(SysSite site, TradePayment payment, String callbackUrl, HttpServletResponse response);

    boolean refund(SysSite site, TradePayment payment, TradeRefund refund);
}
