package com.publiccms.logic.dao.cms;

// Generated 2015-5-8 16:50:23 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsContentAttribute;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsContentAttributeDao extends BaseDao<CmsContentAttribute> {
    public PageHandler getPage(Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContentAttribute bean");
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsContentAttribute init(CmsContentAttribute entity) {
        if (empty(entity.getSource())) {
            entity.setSource(null);
        }
        if (empty(entity.getSourceUrl())) {
            entity.setSourceUrl(null);
        }
        return entity;
    }

}