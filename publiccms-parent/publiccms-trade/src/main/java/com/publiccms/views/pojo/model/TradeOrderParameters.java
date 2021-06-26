package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.entities.trade.TradeOrderProduct;

/**
 *
 * TradeOrderParameters
 * 
 */
public class TradeOrderParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<TradeOrderProduct> tradeOrderProductList;

    /**
     * @return the tradeOrderProductList
     */
    public List<TradeOrderProduct> getTradeOrderProductList() {
        return tradeOrderProductList;
    }

    /**
     * @param tradeOrderProductList
     *            the tradeOrderProductList to set
     */
    public void setTradeOrderProductList(List<TradeOrderProduct> tradeOrderProductList) {
        this.tradeOrderProductList = tradeOrderProductList;
    }
}