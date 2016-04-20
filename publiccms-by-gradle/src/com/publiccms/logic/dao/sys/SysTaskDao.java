package com.publiccms.logic.dao.sys;

// Generated 2015-7-3 16:18:22 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysTask;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysTaskDao extends BaseDao<SysTask> {
    public PageHandler getPage(Integer siteId, Integer status, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysTask bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(status)) {
            queryHandler.condition("bean.status = :status").setParameter("status", status);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysTask init(SysTask entity) {
        return entity;
    }

}