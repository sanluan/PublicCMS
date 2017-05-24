package org.publiccms.logic.service.sys;

import java.util.Date;

import org.publiccms.entities.sys.SysEmailToken;
import org.publiccms.logic.dao.sys.SysEmailTokenDao;
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
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, pageIndex, pageSize);
    }

    /**
     * @param createDate
     * @return
     */
    public int delete(Date createDate) {
        return dao.delete(createDate);
    }
    
    @Autowired
    private SysEmailTokenDao dao;
    
}
