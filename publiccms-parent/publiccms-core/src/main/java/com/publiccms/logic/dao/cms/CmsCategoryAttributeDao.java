package com.publiccms.logic.dao.cms;

import com.publiccms.entities.cms.CmsCategoryAttribute;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;

/**
 *
 * CmsCategoryAttributeDao
 * 
 */
@Repository
public class CmsCategoryAttributeDao extends BaseDao<CmsCategoryAttribute> {

    @Override
    protected CmsCategoryAttribute init(CmsCategoryAttribute entity) {
        return entity;
    }

}