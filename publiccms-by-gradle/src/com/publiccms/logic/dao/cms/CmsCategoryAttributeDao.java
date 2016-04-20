package com.publiccms.logic.dao.cms;

// Generated 2016-1-19 11:41:45 by com.sanluan.common.source.SourceMaker


import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsCategoryAttributeDao extends BaseDao<CmsCategoryAttribute> {
    public PageHandler getPage(Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsCategoryAttribute bean");
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsCategoryAttribute init(CmsCategoryAttribute entity) {
        return entity;
    }

}