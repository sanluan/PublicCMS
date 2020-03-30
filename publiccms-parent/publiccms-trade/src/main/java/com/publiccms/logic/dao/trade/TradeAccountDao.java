package com.publiccms.logic.dao.trade;

// Generated 2019-6-16 9:47:26 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.entities.trade.TradeAccount;

/**
 *
 * TradeAccountDao
 * 
 */
@Repository
public class TradeAccountDao extends BaseDao<TradeAccount> {

    /**
     * 
     * @param siteId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from TradeAccount bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected TradeAccount init(TradeAccount entity) {
        return entity;
    }

}