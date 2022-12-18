package com.publiccms.logic.service.log;

import java.io.Serializable;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.logic.dao.log.LogUploadDao;

/**
 *
 * LogUploadService
 * 
 */
@Service
@Transactional
public class LogUploadService extends BaseService<LogUpload> {

    /**
     * @param siteId
     * @param userId
     * @param channel
     * @param fileTypes 
     * @param originalName 
     * @param filepath
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, String channel, String[] fileTypes, String originalName, String filepath, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, channel, fileTypes, originalName, filepath, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param ids
     */
    public void delete(short siteId, Serializable[] ids) {
        for (LogUpload entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    @Resource
    private LogUploadDao dao;

}