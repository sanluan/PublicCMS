package com.publiccms.logic.service.sys;

// Generated 2016-2-15 21:14:46 by com.sanluan.common.source.SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysFtpUser;
import com.publiccms.logic.dao.sys.SysFtpUserDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysFtpUserService extends BaseService<SysFtpUser> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Integer name, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, name, pageIndex, pageSize);
    }

    public SysFtpUser findByName(String name) {
        return dao.getEntity(name, "name");
    }

    @Autowired
    private SysFtpUserDao dao;
}