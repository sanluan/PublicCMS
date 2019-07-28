package com.publiccms.common.api;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletResponse;

import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeRefund;

public interface PaymentGateway extends Container<String> {
    public String getAccountType();

    public default Supplier<String> keyFunction() {
        return new Supplier<String>() {

            @Override
            public String get() {
                return getAccountType();
            }

        };
    }

    public boolean enable(short siteId);

    public boolean pay(SysSite site, TradeOrder order, String callbackUrl, HttpServletResponse response);

    public boolean refund(SysSite site, TradeOrder order, TradeRefund refund);
}
