package com.publiccms.logic.service.sys;

import java.util.Date;

import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.dao.sys.SysUserTokenDao;

// Generated 2016-1-20 11:19:18 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysUserTokenService
 * 
 */
@Service
@Transactional
public class SysUserTokenService extends BaseService<SysUserToken> {

    /**
     * @param siteId
     * @param userId
     * @param channel
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, String channel, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, userId, channel, orderType, pageIndex, pageSize);
    }
    /**
     * @param userId
     * @return
     */
    public int delete(Long userId) {
        return dao.delete(userId);
    }
    /**
     * @param createDate
     * @return
     */
    public int delete(Date createDate) {
        return dao.delete(createDate);
    }

    @Autowired
    private SysUserTokenDao dao;
    
}