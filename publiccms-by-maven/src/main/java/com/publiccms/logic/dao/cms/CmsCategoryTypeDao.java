package com.publiccms.logic.dao.cms;

// Generated 2016-2-26 15:57:04 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsCategoryType;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsCategoryTypeDao extends BaseDao<CmsCategoryType> {
    public PageHandler getPage(Integer siteId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsCategoryType bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        queryHandler.order("bean.sort asc,bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsCategoryType init(CmsCategoryType entity) {
        return entity;
    }

}