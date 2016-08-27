package com.publiccms.logic.dao.sys;

// Generated 2016-3-2 20:55:08 by com.sanluan.common.source.SourceMaker

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysAppToken;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysAppTokenDao extends BaseDao<SysAppToken> {
    public PageHandler getPage(Integer appId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysAppToken bean");
        if (notEmpty(appId)) {
            queryHandler.condition("bean.appId = :appId").setParameter("appId", appId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public int delete(Date createDate) {
        if (notEmpty(createDate)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysAppToken bean");
            queryHandler.condition("bean.createDate <= :createDate").setParameter("createDate", createDate);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected SysAppToken init(SysAppToken entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}