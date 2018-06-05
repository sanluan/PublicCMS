package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysModule;

/**
 *
 * SysModuleDao
 * 
 */
@Repository
public class SysModuleDao extends BaseDao<SysModule> {
    
    /**
     * @param parentId
     * @param menu
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(String parentId, Boolean menu, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysModule bean");
        if (CommonUtils.notEmpty(parentId)) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (null != menu) {
            queryHandler.condition("bean.menu = :menu").setParameter("menu", menu);
        }
        queryHandler.order("bean.sort asc,bean.id asc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysModule init(SysModule entity) {
        if (CommonUtils.empty(entity.getAuthorizedUrl())) {
            entity.setAuthorizedUrl(null);
        }
        if (CommonUtils.empty(entity.getUrl())) {
            entity.setUrl(null);
        }
        if (CommonUtils.empty(entity.getParentId())) {
            entity.setParentId(null);
        }
        return entity;
    }

}