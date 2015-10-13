package com.publiccms.logic.dao.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker


import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsCategoryAttributeDao extends BaseDao<CmsCategoryAttribute> {
    public PageHandler getPage(Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsCategoryAttribute bean");
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsCategoryAttribute init(CmsCategoryAttribute entity) {
        return entity;
    }

    @Override
    protected Class<CmsCategoryAttribute> getEntityClass() {
        return CmsCategoryAttribute.class;
    }

}