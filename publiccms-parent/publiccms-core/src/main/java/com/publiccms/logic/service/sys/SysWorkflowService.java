package com.publiccms.logic.service.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysWorkflow;
import com.publiccms.logic.dao.sys.SysWorkflowDao;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysWorkflowService
 * 
 */
@Service
@Transactional
public class SysWorkflowService extends BaseService<SysWorkflow> {

    /**
     * @param siteId
     * @param name
     * @param disabled
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, String name, 
                Boolean disabled, 
                String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, name, 
                disabled, 
                orderType, pageIndex, pageSize);
    }
    
    @Resource
    private SysWorkflowDao dao;
    
}