package com.publiccms.common.api;

import java.util.function.Supplier;

import com.publiccms.entities.trade.TradeOrder;

public interface TradeOrderProcessor extends Container<String> {
    String getTradeType();

    default Supplier<String> keyFunction() {
        return () -> getTradeType();
    }

    boolean paid(TradeOrder order);
    
    boolean refunded(TradeOrder order);
}
