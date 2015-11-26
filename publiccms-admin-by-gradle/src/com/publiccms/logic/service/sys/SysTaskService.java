package com.publiccms.logic.service.sys;

// Generated 2015-7-3 16:18:22 by SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.dao.sys.SysTaskDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysTaskService extends BaseService<SysTask> {
    @Autowired
    private SysTaskDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer status, Integer pageIndex, Integer pageSize) {
        return dao.getPage(status, pageIndex, pageSize);
    }

    public SysTask updateStatus(Integer id, int status) {
        SysTask entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setStatus(status);
        }
        return entity;
    }
}