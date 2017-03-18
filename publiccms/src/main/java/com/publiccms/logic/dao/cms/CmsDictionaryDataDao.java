package com.publiccms.logic.dao.cms;

// Generated 2016-11-20 14:50:55 by com.sanluan.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsDictionaryData;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsDictionaryDataDao extends BaseDao<CmsDictionaryData> {
    public PageHandler getPage(
                Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionaryData bean");
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsDictionaryData init(CmsDictionaryData entity) {
        return entity;
    }

}