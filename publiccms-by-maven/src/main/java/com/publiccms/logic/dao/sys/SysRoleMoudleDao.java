package com.publiccms.logic.dao.sys;

// Generated 2015-7-22 13:48:39 by com.sanluan.common.source.SourceMaker

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysRoleMoudle;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysRoleMoudleDao extends BaseDao<SysRoleMoudle> {
    public PageHandler getPage(Integer roleId, Integer moudleId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysRoleMoudle bean");
        if (notEmpty(roleId)) {
            queryHandler.condition("bean.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (notEmpty(moudleId)) {
            queryHandler.condition("bean.moudleId = :moudleId").setParameter("moudleId", moudleId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public SysRoleMoudle getEntity(Integer[] roleIds, Integer moudleId) {
        if (notEmpty(roleIds) && notEmpty(moudleId)) {
            QueryHandler queryHandler = getQueryHandler("from SysRoleMoudle bean");
            queryHandler.condition("bean.roleId in (:roleIds)").setParameter("roleIds", roleIds);
            queryHandler.condition("bean.moudleId = :moudleId").setParameter("moudleId", moudleId);
            return getEntity(queryHandler);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<SysRoleMoudle> getEntitys(Integer[] roleIds, Integer[] moudleIds) {
        if (notEmpty(roleIds) && notEmpty(moudleIds)) {
            QueryHandler queryHandler = getQueryHandler("from SysRoleMoudle bean");
            queryHandler.condition("bean.roleId in (:roleIds)").setParameter("roleIds", roleIds);
            queryHandler.condition("bean.moudleId in (:moudleIds)").setParameter("moudleIds", moudleIds);
            return (List<SysRoleMoudle>) getList(queryHandler);
        }
        return new ArrayList<SysRoleMoudle>();
    }

    public int deleteByRoleId(Integer roleId) {
        if (notEmpty(roleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysRoleMoudle bean where bean.roleId = :roleId");
            queryHandler.setParameter("roleId", roleId);
            return delete(queryHandler);
        }
        return 0;
    }

    public int deleteByMoudleId(Integer moudleId) {
        if (notEmpty(moudleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysRoleMoudle bean where bean.moudleId = :moudleId");
            queryHandler.setParameter("moudleId", moudleId);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected SysRoleMoudle init(SysRoleMoudle entity) {
        return entity;
    }

}