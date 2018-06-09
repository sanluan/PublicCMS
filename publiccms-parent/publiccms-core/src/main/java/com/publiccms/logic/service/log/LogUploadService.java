package com.publiccms.logic.service.log;

import java.io.Serializable;
import java.util.Arrays;

// Generated 2016-5-24 20:56:00 by com.publiccms.common.source.SourceGenerator

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
     * 
     */
    public static final List<String> IMAGE_FILE_SUFFIXS = Arrays.asList(new String[] { ".png", ".jpg", ".jpeg", ".gif", ".bmp" });

    /**
     * 
     */
    public static final List<String> VIDEO_FILE_SUFFIXS = Arrays.asList(new String[] { ".flv", ".swf", ".mkv", ".avi", ".rm",
            ".rmvb", ".mpeg", ".mpg", ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm" });
    /**
     * 
     */
    public static final List<String> MUSIC_FILE_SUFFIXS = Arrays.asList(new String[] { ".mp3", ".wav", ".mid" });
    /**
     * 
     */
    public static final String FILE_TYPE_IMAGE = "image";
    /**
     * 
     */
    public static final String FILE_TYPE_VIDEO = "video";
    /**
     * 
     */
    public static final String FILE_TYPE_MUSIC = "music";
    /**
     * 
     */
    public static final String FILE_TYPE_OTHER = "other";

    public static String getFileType(String suffix) {
        if (IMAGE_FILE_SUFFIXS.contains(suffix)) {
            return FILE_TYPE_IMAGE;
        } else if (VIDEO_FILE_SUFFIXS.contains(suffix)) {
            return FILE_TYPE_VIDEO;
        } else if (MUSIC_FILE_SUFFIXS.contains(suffix)) {
            return FILE_TYPE_MUSIC;
        } else {
            return FILE_TYPE_OTHER;
        }
    }

    /**
     * @param siteId
     * @param userId
     * @param channel
     * @param fileTypes 
     * @param originalName 
     * @param filePath
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, String channel, String[] fileTypes, String originalName, String filePath, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, channel, fileTypes, originalName, filePath, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param createDate
     * @return
     */
    public int delete(Short siteId, Date createDate) {
        return dao.delete(siteId, createDate);
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

    @Autowired
    private LogUploadDao dao;

}