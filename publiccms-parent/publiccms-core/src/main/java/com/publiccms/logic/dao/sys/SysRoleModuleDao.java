package com.publiccms.logic.dao.sys;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRoleModule;

/**
 *
 * SysRoleModuleDao
 * 
 */
@Repository
public class SysRoleModuleDao extends BaseDao<SysRoleModule> {

    /**
     * @param roleId
     * @param moduleId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Integer roleId, String moduleId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysRoleModule bean");
        if (CommonUtils.notEmpty(roleId)) {
            queryHandler.condition("bean.id.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (CommonUtils.notEmpty(moduleId)) {
            queryHandler.condition("bean.id.moduleId = :moduleId").setParameter("moduleId", moduleId);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param roleIds
     * @param moduleId
     * @return entity
     */
    public SysRoleModule getEntity(Integer[] roleIds, String moduleId) {
        if (CommonUtils.notEmpty(roleIds) && CommonUtils.notEmpty(moduleId)) {
            QueryHandler queryHandler = getQueryHandler("from SysRoleModule bean");
            queryHandler.condition("bean.id.roleId in (:roleIds)").setParameter("roleIds", roleIds);
            queryHandler.condition("bean.id.moduleId = :moduleId").setParameter("moduleId", moduleId);
            return getEntity(queryHandler);
        }
        return null;
    }

    /**
     * @param roleIds
     * @param moduleIds
     * @return entitys list
     */
    @SuppressWarnings("unchecked")
    public List<SysRoleModule> getEntitys(Integer[] roleIds, String[] moduleIds) {
        if (CommonUtils.notEmpty(roleIds) && CommonUtils.notEmpty(moduleIds)) {
            QueryHandler queryHandler = getQueryHandler("from SysRoleModule bean");
            queryHandler.condition("bean.id.roleId in (:roleIds)").setParameter("roleIds", roleIds);
            queryHandler.condition("bean.id.moduleId in (:moduleIds)").setParameter("moduleIds", moduleIds);
            return (List<SysRoleModule>) getList(queryHandler);
        }
        return Collections.emptyList();
    }

    /**
     * @param roleId
     * @return number of data deleted
     */
    public int deleteByRoleId(Integer roleId) {
        if (CommonUtils.notEmpty(roleId)) {
            QueryHandler queryHandler = getQueryHandler("delete from SysRoleModule bean where bean.id.roleId = :roleId");
            queryHandler.setParameter("roleId", roleId);
            return delete(queryHandler);
        }
        return 0;
    }

    /**
     * @param moduleId
     * @return number of data deleted
     */
    public int deleteByModuleId(String moduleId) {
        if (CommonUtils.notEmpty(moduleId)) {
            QueryHandler queryHandler = getQueryHandler("delete from SysRoleModule bean where bean.id.moduleId = :moduleId");
            queryHandler.setParameter("moduleId", moduleId);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected SysRoleModule init(SysRoleModule entity) {
        return entity;
    }

}