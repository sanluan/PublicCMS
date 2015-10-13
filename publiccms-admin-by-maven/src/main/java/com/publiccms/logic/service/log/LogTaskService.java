package com.publiccms.logic.service.log;

// Generated 2015-7-3 16:15:25 by SourceMaker

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

    @Autowired
    private LogTaskDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer taskId,Integer pageIndex, Integer pageSize) {
        return dao.getPage(taskId, pageIndex, pageSize);
    }
    
    public int delete(Date createDate) {
        return dao.delete(createDate);
    }
}