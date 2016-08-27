package com.publiccms.logic.service.log;

import java.io.Serializable;

// Generated 2016-5-24 20:56:00 by com.sanluan.common.source.SourceMaker

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.log.LogUpload;
import com.publiccms.logic.dao.log.LogUploadDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class LogUploadService extends BaseService<LogUpload> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, String channel, Boolean image, String filePath, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, channel, image, filePath, orderField, orderType, pageIndex, pageSize);
    }

    public int delete(Integer siteId, Date createDate) {
        return dao.delete(siteId, createDate);
    }

    public void delete(int siteId, Serializable[] ids) {
        for (LogUpload entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    @Autowired
    private LogUploadDao dao;
}