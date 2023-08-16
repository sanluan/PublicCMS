package com.publiccms.logic.service.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysWorkflowStep;
import com.publiccms.logic.dao.sys.SysWorkflowStepDao;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysWorkflowStepService
 * 
 */
@Service
@Transactional
public class SysWorkflowStepService extends BaseService<SysWorkflowStep> {

    /**
     * @param workflowId
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer workflowId, 
                String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(workflowId, 
                orderType, pageIndex, pageSize);
    }
    
    @Resource
    private SysWorkflowStepDao dao;
    
}