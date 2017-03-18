package com.publiccms.logic.dao.cms;

// Generated 2016-11-20 14:50:37 by com.sanluan.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsDictionary;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsDictionaryDao extends BaseDao<CmsDictionary> {
    public PageHandler getPage(Boolean multiple, 
                Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionary bean");
        if (notEmpty(multiple)) {
            queryHandler.condition("bean.multiple = :multiple").setParameter("multiple", multiple);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsDictionary init(CmsDictionary entity) {
        return entity;
    }

}