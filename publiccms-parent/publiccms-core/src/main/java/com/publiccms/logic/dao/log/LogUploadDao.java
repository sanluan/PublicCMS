package com.publiccms.logic.dao.log;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogUpload;

/**
 *
 * LogUploadDao
 * 
 */
@Repository
public class LogUploadDao extends BaseDao<LogUpload> {

    /**
     * @param siteId
     * @param userId
     * @param channel
     * @param privatefile
     * @param fileTypes
     * @param originalName
     * @param filepath
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, String channel, Boolean privatefile, String[] fileTypes,
            String originalName, String filepath, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from LogUpload bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(channel)) {
            queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        }
        if (null != fileTypes) {
            queryHandler.condition("bean.fileType in :fileTypes").setParameter("fileTypes", fileTypes);
        }
        if (null != privatefile) {
            queryHandler.condition("bean.privatefile = :privatefile").setParameter("privatefile", privatefile);
        }
        if (CommonUtils.notEmpty(originalName)) {
            queryHandler.condition("bean.originalName like :originalName").setParameter("originalName", like(originalName));
        }
        if (CommonUtils.notEmpty(filepath)) {
            queryHandler.condition("bean.filepath like :filepath").setParameter("filepath", like(filepath));
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = Constants.BLANK;
        }
        switch (orderField) {
        case "createDate":
            queryHandler.order("bean.createDate").append(orderType);
            break;
        case "fileSize":
            queryHandler.order("bean.fileSize").append(orderType);
            break;
        default:
            queryHandler.order("bean.id").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected LogUpload init(LogUpload entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}