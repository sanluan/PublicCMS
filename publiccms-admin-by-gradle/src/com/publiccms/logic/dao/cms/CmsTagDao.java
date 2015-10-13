package com.publiccms.logic.dao.cms;

// Generated 2015-7-10 16:36:23 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsTag;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsTagDao extends BaseDao<CmsTag> {
    public PageHandler getPage(String name, Integer categoryId, Integer typeId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsTag bean");
        if (notEmpty(name)) {
            queryHandler.condition("bean.name like :name").setParameter("name", likeStart(name));
        }
        if (notEmpty(categoryId)) {
            queryHandler.condition("(bean.categoryId = :categoryId or bean.categoryId is null)").setParameter("categoryId",
                    categoryId);
        }
        if (notEmpty(typeId)) {
            queryHandler.condition("bean.typeId = :typeId").setParameter("typeId", typeId);
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsTag init(CmsTag entity) {
        return entity;
    }

    @Override
    protected Class<CmsTag> getEntityClass() {
        return CmsTag.class;
    }

}