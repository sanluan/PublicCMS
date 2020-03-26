package com.publiccms.logic.dao.cms;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsPlace;

/**
 *
 * CmsPlaceDao
 * 
 */
@Repository
public class CmsPlaceDao extends BaseDao<CmsPlace> {

    /**
     * @param siteId
     * @param userId
     * @param path
     * @param itemType
     * @param itemId
     * @param startPublishDate
     * @param endPublishDate
     * @param expiryDate
     * @param status
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, String path, String itemType, Long itemId, Date startPublishDate,
            Date endPublishDate, Date expiryDate, Integer[] status, Boolean disabled, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsPlace bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(path)) {
            queryHandler.condition("bean.path = :path").setParameter("path", path);
        }
        if (CommonUtils.notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (CommonUtils.notEmpty(itemId)) {
            queryHandler.condition("bean.itemId = :itemId").setParameter("itemId", itemId);
        }
        if (null != startPublishDate) {
            queryHandler.condition("bean.publishDate > :startPublishDate").setParameter("startPublishDate", startPublishDate);
        }
        if (null != endPublishDate) {
            queryHandler.condition("bean.publishDate <= :endPublishDate").setParameter("endPublishDate", endPublishDate);
        }
        if (null != expiryDate) {
            queryHandler.condition("(bean.expiryDate is null or bean.expiryDate >= :expiryDate)").setParameter("expiryDate",
                    expiryDate);
        }
        if (CommonUtils.notEmpty(status)) {
            queryHandler.condition("bean.status in (:status)").setParameter("status", status);
        }
        if (null != disabled) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
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
        case "clicks":
            queryHandler.order("bean.clicks ").append(orderType);
            break;
        default:
            queryHandler.order("bean.publishDate ").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param path
     * @return number of data deleted
     */
    public int delete(short siteId, String path) {
        if (CommonUtils.notEmpty(path)) {
            QueryHandler queryHandler = getQueryHandler("delete from CmsPlace bean");
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            queryHandler.condition("bean.path = :path").setParameter("path", path);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected CmsPlace init(CmsPlace entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        if (null == entity.getPublishDate()) {
            entity.setPublishDate(CommonUtils.getDate());
        }
        return entity;
    }

}