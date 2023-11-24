package com.publiccms.logic.dao.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysWorkflowProcess;

/**
 *
 * SysWorkflowProcessDao
 * 
 */
@Repository
public class SysWorkflowProcessDao extends BaseDao<SysWorkflowProcess> {

    /**
     * @param siteId
     * @param itemType
     * @param itemId
     * @param closed
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, String itemType, String itemId, Boolean closed, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysWorkflowProcess bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (CommonUtils.notEmpty(itemId)) {
            queryHandler.condition("bean.itemId = :itemId").setParameter("itemId", itemId);
        }
        if (null != closed) {
            queryHandler.condition("bean.closed = :closed").setParameter("closed", closed);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysWorkflowProcess init(SysWorkflowProcess entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}