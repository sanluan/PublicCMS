package org.publiccms.logic.dao.cms;

// Generated 2015-12-24 10:49:03 by com.publiccms.common.source.SourceGenerator

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.cms.CmsPlace;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @param status
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, Long userId, String path, String itemType, Integer itemId,
            Date startPublishDate, Date endPublishDate, Integer status, Boolean disabled, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsPlace bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(path)) {
            queryHandler.condition("bean.path = :path").setParameter("path", path);
        }
        if (notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (notEmpty(itemId)) {
            queryHandler.condition("bean.itemId = :itemId").setParameter("itemId", itemId);
        }
        if (notEmpty(startPublishDate)) {
            queryHandler.condition("bean.publishDate > :startPublishDate").setParameter("startPublishDate", startPublishDate);
        }
        if (notEmpty(endPublishDate)) {
            queryHandler.condition("bean.publishDate <= :endPublishDate").setParameter("endPublishDate", endPublishDate);
        }
        if (notEmpty(status)) {
            queryHandler.condition("bean.status = :status").setParameter("status", status);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
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
        case "clicks":
            queryHandler.order("bean.clicks " + orderType);
            break;
        default:
            queryHandler.order("bean.publishDate " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param path
     * @return
     */
    public int delete(int siteId, String path) {
        if (notEmpty(path)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from CmsPlace bean");
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            queryHandler.condition("bean.path = :path").setParameter("path", path);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected CmsPlace init(CmsPlace entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        if (empty(entity.getPublishDate())) {
            entity.setPublishDate(getDate());
        }
        return entity;
    }

}