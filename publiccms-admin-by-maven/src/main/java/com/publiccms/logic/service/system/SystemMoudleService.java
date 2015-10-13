package com.publiccms.logic.service.system;

// Generated 2015-7-22 13:48:39 by SourceMaker


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.system.SystemMoudle;
import com.publiccms.logic.dao.system.SystemMoudleDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SystemMoudleService extends BaseService<SystemMoudle> {

    @Autowired
    private SystemMoudleDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer parentId, String url, 
                Integer pageIndex, Integer pageSize) {
        return dao.getPage(parentId, url, 
                pageIndex, pageSize);
    }
}