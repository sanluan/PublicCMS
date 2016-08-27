package com.publiccms.logic.service.sys;

import java.io.Serializable;

// Generated 2016-3-1 17:24:12 by com.sanluan.common.source.SourceMaker

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.logic.dao.sys.SysAppClientDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysAppClientService extends BaseService<SysAppClient> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, String channel, Long userId, Boolean allowPush, Date startLastLoginDate,
            Date endLastLoginDate, Date startCreateDate, Date endCreateDate, Boolean disabled, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, channel, userId, allowPush, startLastLoginDate, endLastLoginDate, startCreateDate,
                endCreateDate, disabled, orderField, orderType, pageIndex, pageSize);
    }

    public SysAppClient updateStatus(Serializable id, boolean status) {
        SysAppClient entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private SysAppClientDao dao;
}