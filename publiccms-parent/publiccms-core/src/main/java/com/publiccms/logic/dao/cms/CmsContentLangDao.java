package com.publiccms.logic.dao.cms;

import java.util.Collections;
import java.util.List;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.entities.cms.CmsContentLang;

/**
 *
 * CmsContentLangDao
 * 
 */
@Repository
public class CmsContentLangDao extends BaseDao<CmsContentLang> {
    public List<CmsContentLang> getList(Long contentId) {
        if (null != contentId) {
            QueryHandler queryHandler = getQueryHandler("from CmsContentLang bean");
            queryHandler.condition("bean.id.contentId = :contentId").setParameter("contentId", contentId);
            return getEntityList(queryHandler);
        }
        return Collections.emptyList();
    }

    @Override
    protected CmsContentLang init(CmsContentLang entity) {
        return entity;
    }

}