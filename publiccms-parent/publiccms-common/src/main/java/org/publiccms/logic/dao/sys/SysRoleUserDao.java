package org.publiccms.logic.dao.sys;

// Generated 2015-7-20 11:46:39 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.sys.SysRoleUser;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @return
     */
    public PageHandler getPage(Integer roleId, Long userId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysRoleUser bean");
        if (notEmpty(roleId)) {
            queryHandler.condition("bean.id.roleId = :roleId").setParameter("roleId", roleId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.id.userId = :userId").setParameter("userId", userId);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param roleId
     * @return
     */
    public int deleteByRoleId(Integer roleId) {
        if (notEmpty(roleId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysRoleUser bean where bean.id.roleId = :roleId");
            queryHandler.setParameter("roleId", roleId);
            return delete(queryHandler);
        }
        return 0;
    }

    /**
     * @param userId
     * @return
     */
    public int deleteByUserId(Long userId) {
        if (notEmpty(userId)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysRoleUser bean where bean.id.userId = :userId");
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