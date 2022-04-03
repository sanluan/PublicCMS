package com.publiccms.logic.service.sys;

import com.publiccms.entities.sys.SysApp;
import com.publiccms.logic.dao.sys.SysAppDao;

// Generated 2016-3-2 10:25:12 by com.publiccms.common.source.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysAppService
 * 
 */
@Service
@Transactional
public class SysAppService extends BaseService<SysApp> {

    /**
     * @param siteId
     * @param channel
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional
    public PageHandler getPage(Short siteId, String channel, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, channel, pageIndex, pageSize);
    }

    /**
     * @param appKey
     * @return
     */
    public SysApp getEntity(String appKey) {
        return getEntity(appKey, "appKey");
    }

    @Resource
    private SysAppDao dao;
    
}