package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRoleAuthorized;

/**
 *
 * SysRoleAuthorizedDao
 * 
 */
@Repository
public class SysRoleAuthorizedDao extends BaseDao<SysRoleAuthorized> {
    
    /**
     * @param roleId
     * @param url
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Integer roleId, String url, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysRoleAuthorized bean");
        if (CommonUtils.notEmpty(roleId)) {
            queryHandler.condition("bean.id.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (CommonUtils.notEmpty(url)) {
            queryHandler.condition("bean.id.url = :url").setParameter("url", url);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param roleIds
     * @param url
     * @return number of results
     */
    public long count(Integer[] roleIds, String url) {
        QueryHandler queryHandler = getQueryHandler("select count(*) from SysRoleAuthorized bean");
        if (CommonUtils.notEmpty(roleIds)) {
            queryHandler.condition("bean.id.roleId in (:roleIds)").setParameter("roleIds", roleIds);
        }
        if (CommonUtils.notEmpty(url)) {
            queryHandler.condition("bean.id.url = :url").setParameter("url", url);
        }
        return count(queryHandler);
    }

    /**
     * @param roleId
     * @return number of data deleted
     */
    public int deleteByRoleId(Integer roleId) {
        if (CommonUtils.notEmpty(roleId)) {
            QueryHandler queryHandler = getQueryHandler("delete from SysRoleAuthorized bean where bean.id.roleId = :roleId");
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