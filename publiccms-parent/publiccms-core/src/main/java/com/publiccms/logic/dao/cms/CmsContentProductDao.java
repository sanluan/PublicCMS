package com.publiccms.logic.dao.cms;

// Generated 2021-6-26 17:53:08 by com.publiccms.common.generator.SourceGenerator
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContentProduct;

/**
 *
 * CmsContentProductDao
 * 
 */
@Repository
public class CmsContentProductDao extends BaseDao<CmsContentProduct> {

    /**
     * @param siteId
     * @param contentId
     * @param userId
     * @param title
     * @param startPrice
     * @param endPrice
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long contentId, Long userId, String title, BigDecimal startPrice,
            BigDecimal endPrice, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContentProduct bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(contentId)) {
            queryHandler.condition("bean.contentId = :contentId").setParameter("contentId", contentId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (null != startPrice) {
            queryHandler.condition("bean.price > :startPrice").setParameter("startPrice", startPrice);
        }
        if (null != endPrice) {
            queryHandler.condition("bean.price <= :endPrice").setParameter("endPrice", endPrice);
        }
        if (null != title) {
            queryHandler.condition("bean.title like :title").setParameter("title", like(title));
        }
        if (!ORDERTYPE_DESC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_ASC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "price":
            queryHandler.order("bean.price").append(orderType);
            break;
        case "inventory":
            queryHandler.order("bean.inventory").append(orderType);
            break;
        case "sales":
            queryHandler.order("bean.sales").append(orderType);
            break;
        default:
            queryHandler.order("bean.id").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param contentId
     * @return results list
     */
    public List<CmsContentProduct> getList(Short siteId, Long contentId) {
        QueryHandler queryHandler = getQueryHandler("from CmsContentProduct bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(contentId)) {
            queryHandler.condition("bean.contentId = :contentId").setParameter("contentId", contentId);
        }
        return (List<CmsContentProduct>) getList(queryHandler);
    }

    @Override
    protected CmsContentProduct init(CmsContentProduct entity) {
        if (CommonUtils.empty(entity.getCover())) {
            entity.setCover(null);
        }
        return entity;
    }

}