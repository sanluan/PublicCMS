package com.publiccms.logic.dao.system;

// Generated 2015-7-22 13:48:39 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.system.SystemMoudle;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SystemMoudleDao extends BaseDao<SystemMoudle> {
    public PageHandler getPage(Integer parentId, String url, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SystemMoudle bean");
        if (notEmpty(parentId)) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (notEmpty(url)) {
            queryHandler.condition("bean.url = :url").setParameter("url", url);
        }
        queryHandler.append("order by bean.id asc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SystemMoudle init(SystemMoudle entity) {
        return entity;
    }

    @Override
    protected Class<SystemMoudle> getEntityClass() {
        return SystemMoudle.class;
    }

}