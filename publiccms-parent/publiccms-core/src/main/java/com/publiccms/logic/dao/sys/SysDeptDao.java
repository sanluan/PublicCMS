package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDept;

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
     * @param name 
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Integer parentId, Long userId, String name, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysDept bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(parentId)) {
            queryHandler.condition("(bean.parentId = :parentId)").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(name)) {
            queryHandler.condition("(bean.name like :name or bean.code like :name)").setParameter("name", like(name));
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param code
     * @return
     */
    public SysDept getEntityByCode(short siteId, String code) {
        QueryHandler queryHandler = getQueryHandler("from SysDept bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.code = :code").setParameter("code", code);
        return getEntity(queryHandler);
    }

    @Override
    protected SysDept init(SysDept entity) {
        return entity;
    }

}