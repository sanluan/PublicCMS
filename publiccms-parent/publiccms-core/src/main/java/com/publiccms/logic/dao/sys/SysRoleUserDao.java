package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRoleUser;

/**
 *
 * SysRoleUserDao
 * 
 */
@Repository
public class SysRoleUserDao extends BaseDao<SysRoleUser> {
    
    /**
     * @param roleId
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Integer roleId, Long userId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysRoleUser bean");
        if (CommonUtils.notEmpty(roleId)) {
            queryHandler.condition("bean.id.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.id.userId = :userId").setParameter("userId", userId);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param roleId
     * @return number of data deleted
     */
    public int deleteByRoleId(Integer roleId) {
        if (CommonUtils.notEmpty(roleId)) {
            QueryHandler queryHandler = getQueryHandler("delete from SysRoleUser bean where bean.id.roleId = :roleId");
            queryHandler.setParameter("roleId", roleId);
            return delete(queryHandler);
        }
        return 0;
    }

    /**
     * @param userId
     * @return number of data deleted
     */
    public int deleteByUserId(Long userId) {
        if (CommonUtils.notEmpty(userId)) {
            QueryHandler queryHandler = getQueryHandler("delete from SysRoleUser bean where bean.id.userId = :userId");
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