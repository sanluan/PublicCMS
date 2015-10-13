package com.publiccms.logic.dao.system;

// Generated 2015-7-24 16:54:11 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.system.SystemRoleAuthorized;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SystemRoleAuthorizedDao extends BaseDao<SystemRoleAuthorized> {
    public PageHandler getPage(Integer roleId, String authorizedUrl, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SystemRoleAuthorized bean");
        if (notEmpty(roleId)) {
            queryHandler.condition("bean.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (notEmpty(authorizedUrl)) {
            queryHandler.condition("bean.authorizedUrl = :authorizedUrl").setParameter("authorizedUrl", authorizedUrl);
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public int count(Integer[] roleIds, String authorizedUrl) {
        QueryHandler queryHandler = getCountQueryHandler("from SystemRoleAuthorized bean");
        if (notEmpty(roleIds)) {
            queryHandler.condition("bean.roleId in (:roleIds)").setParameter("roleIds", roleIds);
        }
        if (notEmpty(authorizedUrl)) {
            queryHandler.condition("bean.authorizedUrl = :authorizedUrl").setParameter("authorizedUrl", authorizedUrl);
        }
        queryHandler.append("order by bean.id desc");
        return count(queryHandler);
    }

    @Override
    protected SystemRoleAuthorized init(SystemRoleAuthorized entity) {
        return entity;
    }

    @Override
    protected Class<SystemRoleAuthorized> getEntityClass() {
        return SystemRoleAuthorized.class;
    }

}