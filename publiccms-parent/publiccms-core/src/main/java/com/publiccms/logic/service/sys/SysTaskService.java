package com.publiccms.logic.service.sys;

import java.util.Date;

// Generated 2015-7-3 16:18:22 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
     * @param startUpdateDate
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Integer status, Date startUpdateDate, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, status, startUpdateDate, pageIndex, pageSize);
    }

    /**
     * @param id
     * @param status
     * @return 
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean updateStatus(Integer id, int status) {
        SysTask entity = getEntity(id);
        if (null != entity && status != entity.getStatus()) {
            entity.setStatus(status);
            entity.setUpdateDate(new Date());
            return true;
        }
        return false;
    }

    @Resource
    private SysTaskDao dao;

}