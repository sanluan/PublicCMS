package com.publiccms.common.api;

import java.util.function.Supplier;

import com.publiccms.entities.trade.TradeOrder;

public interface TradeOrderProcessor extends Container<String> {
    public String getTradeType();

    public default Supplier<String> keyFunction() {
        return new Supplier<String>() {

            @Override
            public String get() {
                return getTradeType();
            }

        };
    }

    public boolean paid(TradeOrder order);
    
    public boolean refunded(TradeOrder order);
}
