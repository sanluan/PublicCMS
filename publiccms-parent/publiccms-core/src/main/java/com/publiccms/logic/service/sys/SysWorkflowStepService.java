package com.publiccms.logic.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.sys.SysWorkflowStep;
import com.publiccms.logic.dao.sys.SysWorkflowStepDao;

import jakarta.annotation.Resource;

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