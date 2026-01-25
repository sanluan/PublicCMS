package com.publiccms.logic.service.cms;

// Generated 2026-1-25 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsContentSource;
import com.publiccms.logic.dao.cms.CmsContentSourceDao;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsContentSourceService
 * 
 */
@Service
@Transactional
public class CmsContentSourceService extends BaseService<CmsContentSource> {

    /**
     * @param siteId
     * @param name
     * @param userId
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, String name, 
                Long userId, 
                String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, name, 
                userId, 
                orderType, pageIndex, pageSize);
    }
    
    @Resource
    private CmsContentSourceDao dao;
    
}