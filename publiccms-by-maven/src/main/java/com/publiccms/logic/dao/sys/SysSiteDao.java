package com.publiccms.logic.dao.sys;

// Generated 2016-1-20 11:19:19 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysSite;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysSiteDao extends BaseDao<SysSite> {
    public PageHandler getPage(Boolean disabled,String name, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysSite bean");
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (notEmpty(name)) {
            queryHandler.condition("(bean.name like :name)").setParameter("name", like(name));
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysSite init(SysSite entity) {
        return entity;
    }

}