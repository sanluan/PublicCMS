package com.publiccms.logic.dao.cms;

import com.publiccms.entities.cms.CmsDictionary;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsDictionaryDao
 * 
 */
@Repository
public class CmsDictionaryDao extends BaseDao<CmsDictionary> {

    /**
     * @param siteId 
     * @param multiple
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Boolean multiple, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionary bean");
        if (null != siteId) {
            queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (null != multiple) {
            queryHandler.condition("bean.multiple = :multiple").setParameter("multiple", multiple);
        }
        queryHandler.order("bean.id.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsDictionary init(CmsDictionary entity) {
        return entity;
    }

}