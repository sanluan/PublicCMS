package com.publiccms.logic.dao.trade;

import java.util.List;

// Generated 2021-6-26 22:16:13 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.trade.TradeOrderProduct;

/**
 *
 * TradeOrderProductDao
 * 
 */
@Repository
public class TradeOrderProductDao extends BaseDao<TradeOrderProduct> {

    /**
     * @param siteId
     * @param orderId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long orderId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from TradeOrderProduct bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(orderId)) {
            queryHandler.condition("bean.orderId = :orderId").setParameter("orderId", orderId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param orderId
     * @return results list
     */
    @SuppressWarnings("unchecked")
    public List<TradeOrderProduct> getList(Short siteId, Long orderId) {
        QueryHandler queryHandler = getQueryHandler("from TradeOrderProduct bean");
        if (null != siteId && CommonUtils.notEmpty(orderId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            queryHandler.condition("bean.orderId = :orderId").setParameter("orderId", orderId);
            return (List<TradeOrderProduct>) getList(queryHandler);
        }
        return null;
    }

    @Override
    protected TradeOrderProduct init(TradeOrderProduct entity) {
        return entity;
    }

}