package com.publiccms.logic.service.sys;

import java.util.Date;

// Generated 2015-7-3 16:18:22 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.dao.sys.SysTaskDao;

/**
 *
 * SysTaskService
 * 
 */
@Service
@Transactional
public class SysTaskService extends BaseService<SysTask> {

    /**
     * @param siteId
     * @param status
     * @param beginUpdateDate
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Integer status, Date beginUpdateDate, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, status, beginUpdateDate, pageIndex, pageSize);
    }

    /**
     * @param id
     * @param status
     */
    public void updateStatus(Integer id, int status) {
        SysTask entity = getEntity(id);
        if (null != entity) {
            entity.setStatus(status);
        }
    }

    @Autowired
    private SysTaskDao dao;
    
}