package com.publiccms.logic.dao.system;

// Generated 2015-7-22 13:48:39 by SourceMaker

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.system.SystemRoleMoudle;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SystemRoleMoudleDao extends BaseDao<SystemRoleMoudle> {
    public PageHandler getPage(Integer roleId, Integer moudleId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SystemRoleMoudle bean");
        if (notEmpty(roleId)) {
            queryHandler.condition("bean.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (notEmpty(moudleId)) {
            queryHandler.condition("bean.moudleId = :moudleId").setParameter("moudleId", moudleId);
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public SystemRoleMoudle getEntity(Integer[] roleIds, Integer moudleId) {
        if (notEmpty(roleIds) && notEmpty(moudleId)) {
            QueryHandler queryHandler = getQueryHandler("from SystemRoleMoudle bean");
            queryHandler.condition("bean.roleId in (:roleIds)").setParameter("roleIds", roleIds);
            queryHandler.condition("bean.moudleId = :moudleId").setParameter("moudleId", moudleId);
            return getEntity(queryHandler);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<SystemRoleMoudle> getEntitys(Integer[] roleIds, Integer[] moudleIds) {
        if (notEmpty(roleIds) && notEmpty(moudleIds)) {
            QueryHandler queryHandler = getQueryHandler("from SystemRoleMoudle bean");
            queryHandler.condition("bean.roleId in (:roleIds)").setParameter("roleIds", roleIds);
            queryHandler.condition("bean.moudleId in (:moudleIds)").setParameter("moudleIds", moudleIds);
            return (List<SystemRoleMoudle>) getList(queryHandler);
        }
        return new ArrayList<SystemRoleMoudle>();
    }

    public int deleteByRole(Integer roleId) {
        if (notEmpty(roleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SystemRoleMoudle bean where bean.roleId = :roleId");
            queryHandler.setParameter("roleId", roleId);
            return delete(queryHandler);
        } else {
            return 0;
        }
    }

    public int deleteByMoudleId(Integer moudleId) {
        if (notEmpty(moudleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SystemRoleMoudle bean where bean.moudleId = :moudleId");
            queryHandler.setParameter("moudleId", moudleId);
            return delete(queryHandler);
        } else {
            return 0;
        }
    }

    @Override
    protected SystemRoleMoudle init(SystemRoleMoudle entity) {
        return entity;
    }

    @Override
    protected Class<SystemRoleMoudle> getEntityClass() {
        return SystemRoleMoudle.class;
    }

}