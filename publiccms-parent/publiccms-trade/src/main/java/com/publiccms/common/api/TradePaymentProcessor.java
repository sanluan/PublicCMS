package com.publiccms.common.api;

import java.util.function.Supplier;

import com.publiccms.entities.trade.TradePayment;

public interface TradePaymentProcessor extends Container<String> {
    String getTradeType();

    default Supplier<String> keyFunction() {
        return () -> getTradeType();
    }

    boolean paid(TradePayment payment);

    boolean refunded(TradePayment payment);

    boolean cancel(TradePayment payment);
}
