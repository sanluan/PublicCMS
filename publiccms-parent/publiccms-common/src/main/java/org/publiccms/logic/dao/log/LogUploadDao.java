package org.publiccms.logic.dao.log;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.log.LogUpload;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @param image
     * @param filePath
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, Long userId, String channel, Boolean image, String filePath, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from LogUpload bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(channel)) {
            queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        }
        if (notEmpty(image)) {
            queryHandler.condition("bean.image = :image").setParameter("image", image);
        }
        if (notEmpty(filePath)) {
            queryHandler.condition("bean.filePath like :filePath").setParameter("content", like(filePath));
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = BLANK;
        }
        switch (orderField) {
        case "createDate":
            queryHandler.order("bean.createDate " + orderType);
            break;
        case "fileSize":
            queryHandler.order("bean.fileSize " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param createDate
     * @return
     */
    public int delete(Integer siteId, Date createDate) {
        if (notEmpty(siteId) || notEmpty(createDate)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from LogUpload bean");
            if (notEmpty(siteId)) {
                queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            }
            if (notEmpty(createDate)) {
                queryHandler.condition("bean.createDate <= :createDate").setParameter("createDate", createDate);
            }
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected LogUpload init(LogUpload entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}