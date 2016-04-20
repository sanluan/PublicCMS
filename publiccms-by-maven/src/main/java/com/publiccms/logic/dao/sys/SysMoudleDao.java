package com.publiccms.logic.dao.sys;

// Generated 2015-7-22 13:48:39 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysMoudle;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysMoudleDao extends BaseDao<SysMoudle> {
    public PageHandler getPage(Integer parentId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysMoudle bean");
        if (notEmpty(parentId)) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        queryHandler.order("bean.sort asc,bean.id asc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysMoudle init(SysMoudle entity) {
        if (empty(entity.getAuthorizedUrl())) {
            entity.setAuthorizedUrl(null);
        }
        if (empty(entity.getUrl())) {
            entity.setUrl(null);
        }
        return entity;
    }

}