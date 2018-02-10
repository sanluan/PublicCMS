package com.publiccms.logic.dao.cms;

import com.publiccms.entities.cms.CmsPlaceAttribute;

// Generated 2016-3-1 17:29:33 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;

/**
 *
 * CmsPlaceAttributeDao
 * 
 */
@Repository
public class CmsPlaceAttributeDao extends BaseDao<CmsPlaceAttribute> {
    
    @Override
    protected CmsPlaceAttribute init(CmsPlaceAttribute entity) {
        return entity;
    }

}