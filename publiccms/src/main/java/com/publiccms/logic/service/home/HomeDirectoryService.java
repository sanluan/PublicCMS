package com.publiccms.logic.service.home;

import java.io.Serializable;

// Generated 2016-11-13 11:38:13 by com.sanluan.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.home.HomeDirectory;
import com.publiccms.logic.dao.home.HomeDirectoryDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HomeDirectoryService extends BaseService<HomeDirectory> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, Boolean disabled, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, userId, disabled, orderType, pageIndex, pageSize);
    }

    public HomeDirectory updateStatus(Serializable id, boolean status) {
        HomeDirectory entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private HomeDirectoryDao dao;
}