package com.publiccms.logic.dao.sys;

// Generated 2015-7-22 13:48:39 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysMoudle;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysMoudleDao extends BaseDao<SysMoudle> {
    public PageHandler getPage(Integer parentId, String url, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysMoudle bean");
        if (notEmpty(parentId)) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (notEmpty(url)) {
            queryHandler.condition("bean.url = :url").setParameter("url", url);
        }
        queryHandler.order("bean.id asc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysMoudle init(SysMoudle entity) {
        return entity;
    }

    @Override
    protected Class<SysMoudle> getEntityClass() {
        return SysMoudle.class;
    }

}