package com.publiccms.logic.dao.sys;

import java.util.ArrayList;
import java.util.List;

// Generated 2016-3-2 13:39:54 by com.sanluan.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysExtendField;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysExtendFieldDao extends BaseDao<SysExtendField> {
    @SuppressWarnings("unchecked")
    public List<SysExtendField> getList(Integer extendId) {
        if (notEmpty(extendId)) {
            QueryHandler queryHandler = getQueryHandler("from SysExtendField bean");
            queryHandler.condition("bean.id.extendId = :extendId").setParameter("extendId", extendId);
            queryHandler.order("bean.sort asc");
            return (List<SysExtendField>) getList(queryHandler);
        }
        return new ArrayList<SysExtendField>();
    }

    @Override
    protected SysExtendField init(SysExtendField entity) {
        return entity;
    }

}