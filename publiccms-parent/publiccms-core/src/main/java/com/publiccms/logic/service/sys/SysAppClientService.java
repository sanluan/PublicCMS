package com.publiccms.logic.service.sys;

// Generated 2016-3-1 17:24:12 by com.publiccms.common.source.SourceGenerator

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.logic.dao.sys.SysAppClientDao;

/**
 *
 * SysAppClientService
 * 
 */
@Service
@Transactional
public class SysAppClientService extends BaseService<SysAppClient> {

    /**
     * @param siteId
     * @param channel
     * @param userId
     * @param startLastLoginDate
     * @param endLastLoginDate
     * @param startCreateDate
     * @param endCreateDate
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, String channel, Long userId, Date startLastLoginDate, Date endLastLoginDate,
            Date startCreateDate, Date endCreateDate, Boolean disabled, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, channel, userId, startLastLoginDate, endLastLoginDate, startCreateDate, endCreateDate,
                disabled, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId 
     * @param channel 
     * @param uuid 
     * @return the entity
     */
    public SysAppClient getEntity(Short siteId, String channel, String uuid) {
        return dao.getEntity(siteId, channel, uuid);
    }

    /**
     * @param id
     * @param userId
     * @return
     */
    public SysAppClient updateUser(Serializable id, Long userId) {
        SysAppClient entity = getEntity(id);
        if (null != entity) {
            entity.setUserId(userId);
        }
        return entity;
    }

    /**
     * @param id
     * @param clientVersion
     * @param ip
     * @return
     */
    public SysAppClient updateLastLogin(Serializable id, String clientVersion, String ip) {
        SysAppClient entity = getEntity(id);
        if (null != entity) {
            entity.setClientVersion(clientVersion);
            entity.setLastLoginDate(CommonUtils.getDate());
            entity.setLastLoginIp(ip);
        }
        return entity;
    }

    /**
     * @param id
     * @param status
     * @return
     */
    public SysAppClient updateStatus(Serializable id, boolean status) {
        SysAppClient entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private SysAppClientDao dao;

}