package org.publiccms.logic.dao.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.ArrayList;
import java.util.List;

import org.publiccms.entities.sys.SysRoleMoudle;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * SysRoleMoudleDao
 * 
 */
@Repository
public class SysRoleMoudleDao extends BaseDao<SysRoleMoudle> {

    /**
     * @param roleId
     * @param moudleId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer roleId, Integer moudleId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysRoleMoudle bean");
        if (notEmpty(roleId)) {
            queryHandler.condition("bean.id.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (notEmpty(moudleId)) {
            queryHandler.condition("bean.id.moudleId = :moudleId").setParameter("moudleId", moudleId);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param roleIds
     * @param moudleId
     * @return
     */
    public SysRoleMoudle getEntity(Integer[] roleIds, Integer moudleId) {
        if (notEmpty(roleIds) && notEmpty(moudleId)) {
            QueryHandler queryHandler = getQueryHandler("from SysRoleMoudle bean");
            queryHandler.condition("bean.id.roleId in (:roleIds)").setParameter("roleIds", roleIds);
            queryHandler.condition("bean.id.moudleId = :moudleId").setParameter("moudleId", moudleId);
            return getEntity(queryHandler);
        }
        return null;
    }

    /**
     * @param roleIds
     * @param moudleIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SysRoleMoudle> getEntitys(Integer[] roleIds, Integer[] moudleIds) {
        if (notEmpty(roleIds) && notEmpty(moudleIds)) {
            QueryHandler queryHandler = getQueryHandler("from SysRoleMoudle bean");
            queryHandler.condition("bean.id.roleId in (:roleIds)").setParameter("roleIds", roleIds);
            queryHandler.condition("bean.id.moudleId in (:moudleIds)").setParameter("moudleIds", moudleIds);
            return (List<SysRoleMoudle>) getList(queryHandler);
        }
        return new ArrayList<>();
    }

    /**
     * @param roleId
     * @return
     */
    public int deleteByRoleId(Integer roleId) {
        if (notEmpty(roleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysRoleMoudle bean where bean.id.roleId = :roleId");
            queryHandler.setParameter("roleId", roleId);
            return delete(queryHandler);
        }
        return 0;
    }

    /**
     * @param moudleId
     * @return
     */
    public int deleteByMoudleId(Integer moudleId) {
        if (notEmpty(moudleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysRoleMoudle bean where bean.id.moudleId = :moudleId");
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