package com.publiccms.logic.dao.cms;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.entities.cms.CmsCategoryLang;

/**
 *
 * CmsCategoryLangDao
 * 
 */
@Repository
public class CmsCategoryLangDao extends BaseDao<CmsCategoryLang> {

    @Override
    protected CmsCategoryLang init(CmsCategoryLang entity) {
        return entity;
    }

}