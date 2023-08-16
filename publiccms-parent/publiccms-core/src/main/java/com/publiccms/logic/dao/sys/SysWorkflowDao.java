package com.publiccms.logic.dao.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysWorkflow;

/**
 *
 * SysWorkflowDao
 * 
 */
@Repository
public class SysWorkflowDao extends BaseDao<SysWorkflow> {

    /**
     * @param siteId
     * @param name
     * @param disabled
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, String name, Boolean disabled, String orderType, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysWorkflow bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(name)) {
            queryHandler.condition("(bean.name like :name or bean.description like :name)").setParameter("name", like(name));
        }
        if (null != disabled) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.steps").append(orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysWorkflow init(SysWorkflow entity) {
        return entity;
    }

}