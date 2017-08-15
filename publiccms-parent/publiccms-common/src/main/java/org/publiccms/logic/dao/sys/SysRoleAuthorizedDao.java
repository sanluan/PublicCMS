package org.publiccms.logic.dao.sys;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.sys.SysRoleAuthorized;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @return
     */
    public PageHandler getPage(Integer roleId, String url, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysRoleAuthorized bean");
        if (notEmpty(roleId)) {
            queryHandler.condition("bean.id.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (notEmpty(url)) {
            queryHandler.condition("bean.id.url = :url").setParameter("url", url);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param roleIds
     * @param url
     * @return
     */
    public int count(Integer[] roleIds, String url) {
        QueryHandler queryHandler = getCountQueryHandler("from SysRoleAuthorized bean");
        if (notEmpty(roleIds)) {
            queryHandler.condition("bean.id.roleId in (:roleIds)").setParameter("roleIds", roleIds);
        }
        if (notEmpty(url)) {
            queryHandler.condition("bean.id.url = :url").setParameter("url", url);
        }
        return count(queryHandler);
    }

    /**
     * @param roleId
     * @return
     */
    public int deleteByRoleId(Integer roleId) {
        if (notEmpty(roleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysRoleAuthorized bean where bean.id.roleId = :roleId");
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