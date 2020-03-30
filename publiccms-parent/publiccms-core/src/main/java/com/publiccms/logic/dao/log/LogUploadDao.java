package com.publiccms.logic.dao.log;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
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
     * @param fileTypes
     * @param originalName
     * @param filePath
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, String channel, String[] fileTypes, String originalName, String filePath,
            String orderField, String orderType, Integer pageIndex, Integer pageSize) {
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
        if (CommonUtils.notEmpty(originalName)) {
            queryHandler.condition("bean.originalName like :originalName").setParameter("originalName", like(originalName));
        }
        if (CommonUtils.notEmpty(filePath)) {
            queryHandler.condition("bean.filePath like :filePath").setParameter("filePath", like(filePath));
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "createDate":
            queryHandler.order("bean.createDate ").append(orderType);
            break;
        case "fileSize":
            queryHandler.order("bean.fileSize ").append(orderType);
            break;
        default:
            queryHandler.order("bean.id ").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param createDate
     * @return number of data deleted
     */
    public int delete(Short siteId, Date createDate) {
        if (CommonUtils.notEmpty(siteId) || null != createDate) {
            QueryHandler queryHandler = getQueryHandler("delete from LogUpload bean");
            if (CommonUtils.notEmpty(siteId)) {
                queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            }
            if (null != createDate) {
                queryHandler.condition("bean.createDate <= :createDate").setParameter("createDate", createDate);
            }
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected LogUpload init(LogUpload entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}