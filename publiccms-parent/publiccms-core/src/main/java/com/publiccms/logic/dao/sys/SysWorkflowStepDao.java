package com.publiccms.logic.dao.sys;

import java.util.Collections;
import java.util.List;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
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
     * @param sort 
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public List<SysWorkflowStep> getList(Integer workflowId, Integer sort) {
        if (CommonUtils.notEmpty(workflowId)) {
            QueryHandler queryHandler = getQueryHandler("from SysWorkflowStep bean");
            queryHandler.condition("bean.workflowId = :workflowId").setParameter("workflowId", workflowId);
            if (null != sort) {
                queryHandler.condition("bean.sort > :sort").setParameter("sort", sort);
            }
            queryHandler.order("bean.sort asc");
            return getEntityList(queryHandler);
        }
        return Collections.emptyList();
    }

    public void deleteByWorkflowId(Long workflowId) {
        if (null != workflowId) {
            QueryHandler queryHandler = getQueryHandler("delete from SysWorkflowStep bean");
            queryHandler.condition("bean.workflowId = :workflowId").setParameter("workflowId", workflowId);
            delete(queryHandler);
        }
    }

    @Override
    protected SysWorkflowStep init(SysWorkflowStep entity) {
        return entity;
    }

}