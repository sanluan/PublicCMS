package com.publiccms.logic.dao.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysWorkflowStep;

/**
 *
 * SysWorkflowStepDao
 * 
 */
@Repository
public class SysWorkflowStepDao extends BaseDao<SysWorkflowStep> {

    /**
     * @param workflowId
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Integer workflowId, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysWorkflowStep bean");
        if (CommonUtils.notEmpty(workflowId)) {
            queryHandler.condition("bean.workflowId = :workflowId").setParameter("workflowId", workflowId);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.sort").append(orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysWorkflowStep init(SysWorkflowStep entity) {
        return entity;
    }

}