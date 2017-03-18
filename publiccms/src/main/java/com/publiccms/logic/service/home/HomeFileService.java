package com.publiccms.logic.service.home;

import java.io.Serializable;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.home.HomeFile;
import com.publiccms.logic.dao.home.HomeFileDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HomeFileService extends BaseService<HomeFile> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, Long directoryId, String title, String filePath, Boolean image,
            Boolean disabled, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, directoryId, title, filePath, image, disabled, orderField, orderType, pageIndex,
                pageSize);
    }

    public HomeFile updateStatus(Serializable id, boolean status) {
        HomeFile entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private HomeFileDao dao;
}