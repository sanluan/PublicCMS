package com.publiccms.logic.service.log;

import java.io.Serializable;

// Generated 2015-7-3 16:15:25 by com.sanluan.common.source.SourceMaker

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.log.LogTask;
import com.publiccms.logic.dao.log.LogTaskDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class LogTaskService extends BaseService<LogTask> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Integer taskId, Date startBegintime, Date endBegintime, Boolean success,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, taskId, startBegintime, endBegintime, success, orderType, pageIndex, pageSize);
    }

    public int delete(Integer siteId, Date begintime) {
        return dao.delete(siteId, begintime);
    }

    public void delete(int siteId, Serializable[] ids) {
        for (LogTask entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    @Autowired
    private LogTaskDao dao;
}