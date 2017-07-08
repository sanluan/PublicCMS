package org.publiccms.logic.service.sys;

// Generated 2016-3-2 20:55:08 by com.publiccms.common.source.SourceGenerator

import java.util.Date;

import org.publiccms.entities.sys.SysAppToken;
import org.publiccms.logic.dao.sys.SysAppTokenDao;
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
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer appId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(appId, pageIndex, pageSize);
    }

    /**
     * @param createDate
     * @return
     */
    public int delete(Date createDate) {
        return dao.delete(createDate);
    }

    @Autowired
    private SysAppTokenDao dao;
    
}