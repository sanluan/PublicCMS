package org.publiccms.logic.dao.sys;

// Generated 2015-7-20 11:46:39 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.sys.SysDept;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * SysDeptDao
 * 
 */
@Repository
public class SysDeptDao extends BaseDao<SysDept> {

    /**
     * @param siteId
     * @param parentId
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, Integer parentId, Long userId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysDept bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(parentId)) {
            queryHandler.condition("(bean.parentId = :parentId)").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (notEmpty(userId)) {
            queryHandler.condition("(bean.userId = :userId)").setParameter("userId", userId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysDept init(SysDept entity) {
        return entity;
    }

}