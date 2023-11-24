package com.publiccms.logic.service.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysWorkflowProcess;
import com.publiccms.logic.dao.sys.SysWorkflowProcessDao;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysWorkflowProcessService
 * 
 */
@Service
@Transactional
public class SysWorkflowProcessService extends BaseService<SysWorkflowProcess> {

    /**
     * @param siteId
     * @param itemType
     * @param itemId
     * @param closed
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, String itemType, 
                String itemId, Boolean closed, 
                Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, itemType, 
                itemId, closed, 
                pageIndex, pageSize);
    }
    
    @Resource
    private SysWorkflowProcessDao dao;
    
}