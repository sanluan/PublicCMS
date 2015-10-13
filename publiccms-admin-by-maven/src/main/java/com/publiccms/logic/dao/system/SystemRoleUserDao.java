package com.publiccms.logic.dao.system;

// Generated 2015-7-20 11:46:39 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.system.SystemRoleUser;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SystemRoleUserDao extends BaseDao<SystemRoleUser> {
    public PageHandler getPage(Integer roleId, Integer userId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SystemRoleUser bean");
        if (notEmpty(roleId)) {
            queryHandler.condition("bean.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public int deleteByRole(Integer roleId) {
        if (notEmpty(roleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SystemRoleUser bean where bean.roleId = :roleId");
            queryHandler.setParameter("roleId", roleId);
            return delete(queryHandler);
        } else {
            return 0;
        }
    }

    public int deleteByUserId(Integer userId) {
        if (notEmpty(userId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SystemRoleUser bean where bean.userId = :userId");
            queryHandler.setParameter("userId", userId);
            return delete(queryHandler);
        } else {
            return 0;
        }
    }
    
    @Override
    protected SystemRoleUser init(SystemRoleUser entity) {
        return entity;
    }

    @Override
    protected Class<SystemRoleUser> getEntityClass() {
        return SystemRoleUser.class;
    }

}