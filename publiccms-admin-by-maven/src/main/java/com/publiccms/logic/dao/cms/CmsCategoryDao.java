package com.publiccms.logic.dao.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsCategory;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsCategoryDao extends BaseDao<CmsCategory> {
    public PageHandler getPage(Integer parentId, String extend1, String name, String extend3, String extend2, String extend4,
            Boolean disabled, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsCategory bean");
        if (notEmpty(parentId)) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (notEmpty(extend1)) {
            queryHandler.condition("bean.extend1 = :extend1").setParameter("extend1", extend1);
        }
        if (notEmpty(name)) {
            queryHandler.condition("bean.name like :name").setParameter("name", like(name));
        }
        if (notEmpty(extend3)) {
            queryHandler.condition("bean.extend3 = :extend3").setParameter("extend3", extend3);
        }
        if (notEmpty(extend2)) {
            queryHandler.condition("bean.extend2 = :extend2").setParameter("extend2", extend2);
        }
        if (notEmpty(extend4)) {
            queryHandler.condition("bean.extend4 = :extend4").setParameter("extend4", extend4);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public int delete(Integer[] categoryIds) {
        QueryHandler queryHandler = getQueryHandler("update CmsCategory bean set bean.disabled = :disabled");
        queryHandler.condition("bean.id in (:categoryIds)").setParameter("categoryIds", categoryIds)
                .setParameter("disabled", true);
        return update(queryHandler);
    }

    @Override
    protected CmsCategory init(CmsCategory entity) {
        return entity;
    }

    @Override
    protected Class<CmsCategory> getEntityClass() {
        return CmsCategory.class;
    }

}