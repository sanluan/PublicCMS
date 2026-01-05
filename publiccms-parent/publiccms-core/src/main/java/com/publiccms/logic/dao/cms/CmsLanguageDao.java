package com.publiccms.logic.dao.cms;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsLanguage;

/**
 *
 * CmsLanguageDao
 * 
 */
@Repository
public class CmsLanguageDao extends BaseDao<CmsLanguage> {

    /**
     * @param siteId
     * @return results page
     */
    public List<CmsLanguage> getList(Short siteId) {
        QueryHandler queryHandler = getQueryHandler("from CmsLanguage bean");
        if (null != siteId) {
            queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        }
        queryHandler.order("bean.sort desc");
        return getEntityList(queryHandler);
    }

    @Override
    protected CmsLanguage init(CmsLanguage entity) {
        if (CommonUtils.empty(entity.getCover())) {
            entity.setCover(null);
        }
        return entity;
    }

}