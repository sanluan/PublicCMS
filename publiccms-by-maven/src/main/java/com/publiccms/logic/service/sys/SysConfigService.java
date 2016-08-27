package com.publiccms.logic.service.sys;

// Generated 2016-7-16 11:54:15 by com.sanluan.common.source.SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysConfig;
import com.publiccms.logic.dao.sys.SysConfigDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysConfigService extends BaseService<SysConfig> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, String code, String subcode, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, code, subcode, pageIndex, pageSize);
    }

    @Transactional(readOnly = true)
    public SysConfig getEntity(int siteId, String code, String subcode) {
        return dao.getEntity(siteId, code, subcode);
    }

    @Autowired
    private SysConfigDao dao;
}