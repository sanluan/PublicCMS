package com.publiccms.logic.service.sys;

// Generated 2016-3-2 20:55:08 by com.publiccms.common.source.SourceGenerator

import java.util.Date;

import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.logic.dao.sys.SysAppTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysAppTokenService
 * 
 */
@Service
@Transactional
public class SysAppTokenService extends BaseService<SysAppToken> {

    /**
     * @param appId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer appId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(appId, pageIndex, pageSize);
    }

    /**
     * @param now
     * @return number of data deleted
     */
    public int delete(Date now) {
        return dao.delete(now);
    }

    /**
     * @param appToken 
     * @param expiryDate 
     * @return entity
     */
    public SysAppToken updateExpiryDate(String appToken, Date expiryDate) {
        SysAppToken entity = getEntity(appToken);
        if (null != entity) {
            entity.setExpiryDate(expiryDate);
        }
        return entity;
    }

    @Autowired
    private SysAppTokenDao dao;

}