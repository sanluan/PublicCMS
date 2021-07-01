package com.publiccms.common.api;

import java.util.function.Supplier;

import com.publiccms.entities.trade.TradePayment;

public interface TradePaymentProcessor extends Container<String> {
    String getTradeType();

    default Supplier<String> keyFunction() {
        return () -> getTradeType();
    }

    boolean paid(short siteId, TradePayment payment);

    boolean refunded(short siteId, TradePayment payment);

    boolean cancel(short siteId, TradePayment payment);
}
