package com.publiccms.logic.dao.sys;

// Generated 2016-3-2 10:25:12 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysApp;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysAppDao extends BaseDao<SysApp> {
    public PageHandler getPage(Integer siteId, String channel, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysApp bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(channel)) {
            queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysApp init(SysApp entity) {
        return entity;
    }

}