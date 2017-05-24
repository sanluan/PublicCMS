package org.publiccms.logic.service.log;

import java.io.Serializable;

// Generated 2016-5-24 20:56:00 by com.publiccms.common.source.SourceGenerator

import java.util.Date;

import org.publiccms.entities.log.LogUpload;
import org.publiccms.logic.dao.log.LogUploadDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

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
     * @param image
     * @param filePath
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, String channel, Boolean image, String filePath, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, channel, image, filePath, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param createDate
     * @return
     */
    public int delete(Integer siteId, Date createDate) {
        return dao.delete(siteId, createDate);
    }

    /**
     * @param siteId
     * @param ids
     */
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