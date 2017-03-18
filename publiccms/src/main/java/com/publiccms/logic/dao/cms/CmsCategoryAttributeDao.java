package com.publiccms.logic.dao.cms;

// Generated 2016-1-19 11:41:45 by com.sanluan.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.sanluan.common.base.BaseDao;

@Repository
public class CmsCategoryAttributeDao extends BaseDao<CmsCategoryAttribute> {

    @Override
    protected CmsCategoryAttribute init(CmsCategoryAttribute entity) {
        return entity;
    }

}