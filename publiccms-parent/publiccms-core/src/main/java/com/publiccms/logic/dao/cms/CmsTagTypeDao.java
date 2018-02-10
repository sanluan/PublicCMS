package com.publiccms.logic.dao.cms;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsTagType;

/**
 * 标签类型DAO
 * 
 * CmsTagType DAO
 * 
 */
@Repository
public class CmsTagTypeDao extends BaseDao<CmsTagType> {
    /**
     * 获取标签类型列表
     * 
     * Get tag type list
     * 
     * @param siteId
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, String name, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsTagType bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(name)) {
            queryHandler.condition("bean.name like :name").setParameter("name", rightLike(name));
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsTagType init(CmsTagType entity) {
        return entity;
    }

}