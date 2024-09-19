package com.publiccms.logic.dao.cms;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsTag;

/**
 * 标签DAO
 * 
 * CmsTag DAO
 * 
 */
@Repository
public class CmsTagDao extends BaseDao<CmsTag> {
    /**
     * 获取标签列表
     * 
     * Get tag list
     * 
     * @param siteId
     * @param typeId
     * @param name
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Integer typeId, String name, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsTag bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(typeId)) {
            queryHandler.condition("bean.typeId = :typeId").setParameter("typeId", typeId);
        }
        if (CommonUtils.notEmpty(name)) {
            queryHandler.condition("bean.name like :name").setParameter("name", rightLike(name));
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = Constants.BLANK;
        }
        switch (orderField) {
        case "searchCount":
            queryHandler.order("bean.searchCount").append(orderType);
            break;
        default:
            queryHandler.order("bean.id").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * 获取标签数量
     * 
     * Get tag count
     * 
     * @param siteId
     * @param typeId
     * @return results count
     */
    public int getCount(Short siteId, Integer typeId) {
        QueryHandler queryHandler = getQueryHandler("select count(*) from CmsTag bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(typeId)) {
            queryHandler.condition("bean.typeId = :typeId").setParameter("typeId", typeId);
        }
        return (int) count(queryHandler);
    }

    @Override
    protected CmsTag init(CmsTag entity) {
        return entity;
    }

}