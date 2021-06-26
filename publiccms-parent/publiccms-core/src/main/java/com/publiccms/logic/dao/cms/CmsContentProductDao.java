package com.publiccms.logic.dao.cms;

// Generated 2021-6-26 17:53:08 by com.publiccms.common.generator.SourceGenerator
import java.math.BigDecimal;

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
     * @param contentId
     * @param userId
     * @param price
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long contentId, Long userId, BigDecimal price, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContentProduct bean");
        if (CommonUtils.notEmpty(contentId)) {
            queryHandler.condition("bean.contentId = :contentId").setParameter("contentId", contentId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (null != price) {
            queryHandler.condition("bean.price = :price").setParameter("price", price);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "price":
            queryHandler.order("bean.price ").append(orderType);
            break;
        case "inventory":
            queryHandler.order("bean.inventory ").append(orderType);
            break;
        case "sales":
            queryHandler.order("bean.sales ").append(orderType);
            break;
        default:
            queryHandler.order("bean.id ").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsContentProduct init(CmsContentProduct entity) {
        return entity;
    }

}