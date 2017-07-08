package org.publiccms.logic.dao.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.sys.SysMoudle;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * SysMoudleDao
 * 
 */
@Repository
public class SysMoudleDao extends BaseDao<SysMoudle> {
    
    /**
     * @param parentId
     * @param menu
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer parentId, Boolean menu, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysMoudle bean");
        if (notEmpty(parentId)) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (notEmpty(menu)) {
            queryHandler.condition("bean.menu = :menu").setParameter("menu", menu);
        }
        queryHandler.order("bean.sort asc,bean.id asc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysMoudle init(SysMoudle entity) {
        if (empty(entity.getAuthorizedUrl())) {
            entity.setAuthorizedUrl(null);
        }
        if (empty(entity.getUrl())) {
            entity.setUrl(null);
        }
        return entity;
    }

}