package com.publiccms.logic.dao.sys;

// Generated 2016-2-15 21:14:44 by com.sanluan.common.source.SourceMaker


import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysFtpUser;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysFtpUserDao extends BaseDao<SysFtpUser> {
    public PageHandler getPage(Integer siteId, Integer name, 
                Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysFtpUser bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(name)) {
            queryHandler.condition("bean.name = :name").setParameter("name", name);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysFtpUser init(SysFtpUser entity) {
        return entity;
    }

}