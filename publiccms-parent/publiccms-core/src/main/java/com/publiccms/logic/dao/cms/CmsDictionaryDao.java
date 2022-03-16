package com.publiccms.logic.dao.cms;

import com.publiccms.entities.cms.CmsDictionary;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * CmsDictionaryDao
 * 
 */
@Repository
public class CmsDictionaryDao extends BaseDao<CmsDictionary> {

    /**
     * @param siteId
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, String name, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionary bean");
        if (null != siteId) {
            queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(name)) {
            queryHandler.condition("bean.name like :name").setParameter("name", like(name));
        }
        queryHandler.order("bean.id.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsDictionary init(CmsDictionary entity) {
        return entity;
    }

}