package com.publiccms.logic.dao.sys;

// Generated 2015-7-24 16:54:11 by com.sanluan.common.source.SourceMaker

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysRoleAuthorized;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysRoleAuthorizedDao extends BaseDao<SysRoleAuthorized> {
    public PageHandler getPage(Integer roleId, String url, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysRoleAuthorized bean");
        if (notEmpty(roleId)) {
            queryHandler.condition("bean.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (notEmpty(url)) {
            queryHandler.condition("bean.url = :url").setParameter("url", url);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @SuppressWarnings("unchecked")
    public List<SysRoleAuthorized> getEntitys(Integer[] roleIds, String[] urls) {
        if (notEmpty(roleIds) && notEmpty(urls)) {
            QueryHandler queryHandler = getQueryHandler("from SysRoleAuthorized bean");
            queryHandler.condition("bean.roleId in (:roleIds)").setParameter("roleIds", roleIds);
            queryHandler.condition("bean.url in (:urls)").setParameter("urls", urls);
            return (List<SysRoleAuthorized>) getList(queryHandler);
        }
        return new ArrayList<SysRoleAuthorized>();
    }

    public int count(Integer[] roleIds, String url) {
        QueryHandler queryHandler = getCountQueryHandler("from SysRoleAuthorized bean");
        if (notEmpty(roleIds)) {
            queryHandler.condition("bean.roleId in (:roleIds)").setParameter("roleIds", roleIds);
        }
        if (notEmpty(url)) {
            queryHandler.condition("bean.url = :url").setParameter("url", url);
        }
        return count(queryHandler);
    }

    public int deleteByRoleId(Integer roleId) {
        if (notEmpty(roleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysRoleAuthorized bean where bean.roleId = :roleId");
            queryHandler.setParameter("roleId", roleId);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected SysRoleAuthorized init(SysRoleAuthorized entity) {
        return entity;
    }

}