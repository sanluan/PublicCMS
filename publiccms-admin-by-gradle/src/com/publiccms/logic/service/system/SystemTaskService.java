package com.publiccms.logic.service.system;

// Generated 2015-7-3 16:18:22 by SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.system.SystemTask;
import com.publiccms.logic.dao.system.SystemTaskDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SystemTaskService extends BaseService<SystemTask> {
    @Autowired
    private SystemTaskDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer status, Integer pageIndex, Integer pageSize) {
        return dao.getPage(status, pageIndex, pageSize);
    }

    public SystemTask updateStatus(Integer id, int status) {
        SystemTask entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setStatus(status);
        }
        return entity;
    }
}