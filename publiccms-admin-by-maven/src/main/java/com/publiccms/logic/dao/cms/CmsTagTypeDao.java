package com.publiccms.logic.dao.cms;

// Generated 2015-7-10 16:36:23 by SourceMaker


import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsTagType;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsTagTypeDao extends BaseDao<CmsTagType> {
    public PageHandler getPage(
                Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsTagType bean");
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsTagType init(CmsTagType entity) {
        return entity;
    }

    @Override
    protected Class<CmsTagType> getEntityClass() {
        return CmsTagType.class;
    }

}