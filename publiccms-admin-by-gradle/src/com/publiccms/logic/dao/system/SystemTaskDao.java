package com.publiccms.logic.dao.system;

// Generated 2015-7-3 16:18:22 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.system.SystemTask;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SystemTaskDao extends BaseDao<SystemTask> {
    public PageHandler getPage(Integer status, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SystemTask bean");
        if (notEmpty(status)) {
            queryHandler.condition("bean.status = :status").setParameter("status", status);
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SystemTask init(SystemTask entity) {
        return entity;
    }

    @Override
    protected Class<SystemTask> getEntityClass() {
        return SystemTask.class;
    }

}