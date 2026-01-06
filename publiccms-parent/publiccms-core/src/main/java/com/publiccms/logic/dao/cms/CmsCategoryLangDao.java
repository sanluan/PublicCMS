package com.publiccms.logic.dao.cms;

import java.util.Collections;
import java.util.List;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.entities.cms.CmsCategoryLang;

/**
 *
 * CmsCategoryLangDao
 * 
 */
@Repository
public class CmsCategoryLangDao extends BaseDao<CmsCategoryLang> {
    public List<CmsCategoryLang> getList(Integer categoryId) {
        if (null != categoryId) {
            QueryHandler queryHandler = getQueryHandler("from CmsCategoryLang bean");
            queryHandler.condition("bean.id.categoryId = :categoryId").setParameter("categoryId", categoryId);
            return getEntityList(queryHandler);
        }
        return Collections.emptyList();
    }

    @Override
    protected CmsCategoryLang init(CmsCategoryLang entity) {
        return entity;
    }

}