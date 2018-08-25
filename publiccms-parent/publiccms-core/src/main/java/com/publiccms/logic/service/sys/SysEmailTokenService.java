package com.publiccms.logic.service.sys;

import java.util.Date;

import com.publiccms.entities.sys.SysEmailToken;
import com.publiccms.logic.dao.sys.SysEmailTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysEmailTokenService
 * 
 */
@Service
@Transactional
public class SysEmailTokenService extends BaseService<SysEmailToken> {

    /**
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, pageIndex, pageSize);
    }

    /**
     * @param now
     * @return number of data deleted
     */
    public int delete(Date now) {
        return dao.delete(now);
    }
    
    @Autowired
    private SysEmailTokenDao dao;
    
}
