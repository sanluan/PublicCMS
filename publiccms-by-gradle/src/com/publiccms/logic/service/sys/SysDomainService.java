package com.publiccms.logic.service.sys;

// Generated 2015-7-3 16:18:22 by com.sanluan.common.source.SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysDomain;
import com.publiccms.logic.dao.sys.SysDomainDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysDomainService extends BaseService<SysDomain> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, pageIndex, pageSize);
    }

    public SysDomain getEntity(String name) {
        return getEntity(name, "name");
    }
    
    @Autowired
    private SysDomainDao dao;
}