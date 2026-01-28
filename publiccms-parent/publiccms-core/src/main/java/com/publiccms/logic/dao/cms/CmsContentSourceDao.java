package com.publiccms.logic.dao.cms;

// Generated 2026-1-25 by com.publiccms.common.generator.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContentSource;
/**
 *
 * CmsContentSourceDao
 * 
 */
@Repository
public class CmsContentSourceDao extends BaseDao<CmsContentSource> {
    
    /**
     * @param siteId
     * @param name
     * @param userId
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, String name, 
                Long userId, 
                String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContentSource bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(name)) {
            queryHandler.condition("(bean.name like :name or bean.url like :name or bean.initial like :name)").setParameter("name", like(name));
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if(!ORDERTYPE_ASC.equalsIgnoreCase(orderType)){
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.createDate").append(orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsContentSource init(CmsContentSource entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.now());
        }
        return entity;
    }

}