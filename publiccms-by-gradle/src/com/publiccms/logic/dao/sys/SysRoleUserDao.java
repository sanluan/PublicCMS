package com.publiccms.logic.dao.sys;

// Generated 2015-7-20 11:46:39 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysRoleUser;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysRoleUserDao extends BaseDao<SysRoleUser> {
    public PageHandler getPage(Integer roleId, Long userId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysRoleUser bean");
        if (notEmpty(roleId)) {
            queryHandler.condition("bean.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public int deleteByRoleId(Integer roleId) {
        if (notEmpty(roleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysRoleUser bean where bean.roleId = :roleId");
            queryHandler.setParameter("roleId", roleId);
            return delete(queryHandler);
        }
        return 0;
    }

    public int deleteByUserId(Long userId) {
        if (notEmpty(userId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysRoleUser bean where bean.userId = :userId");
            queryHandler.setParameter("userId", userId);
            return delete(queryHandler);
        }
        return 0;
    }
    
    @Override
    protected SysRoleUser init(SysRoleUser entity) {
        return entity;
    }

}