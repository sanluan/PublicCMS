package com.publiccms.logic.dao.system;

// Generated 2015-7-20 11:46:39 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.system.SystemRole;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SystemRoleDao extends BaseDao<SystemRole> {
    public PageHandler getPage(Integer deptId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SystemRole bean");
        if (notEmpty(deptId)) {
            queryHandler.condition("(bean.deptId = :deptId)").setParameter("deptId", deptId);
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SystemRole init(SystemRole entity) {
        return entity;
    }

    @Override
    protected Class<SystemRole> getEntityClass() {
        return SystemRole.class;
    }

}