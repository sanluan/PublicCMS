package com.publiccms.logic.dao.cms;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.entities.cms.CmsContentLang;

/**
 *
 * CmsContentLangDao
 * 
 */
@Repository
public class CmsContentLangDao extends BaseDao<CmsContentLang> {

    @Override
    protected CmsContentLang init(CmsContentLang entity) {
        return entity;
    }

}