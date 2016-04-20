package com.publiccms.logic.dao.sys;

// Generated 2016-3-2 13:39:54 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysExtendField;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysExtendFieldDao extends BaseDao<SysExtendField> {
    public PageHandler getPage(Integer extendId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysExtendField bean");
        if (notEmpty(extendId)) {
            queryHandler.condition("bean.extendId = :extendId").setParameter("extendId", extendId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysExtendField init(SysExtendField entity) {
        return entity;
    }

}