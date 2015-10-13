package com.publiccms.logic.dao.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsCategoryModel;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsCategoryModelDao extends BaseDao<CmsCategoryModel> {
    public PageHandler getPage(Integer modelId, Integer categoryId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsCategoryModel bean");
        if (notEmpty(modelId)) {
            queryHandler.condition("bean.modelId = :modelId").setParameter("modelId", modelId);
        }
        if (notEmpty(categoryId)) {
            queryHandler.condition("bean.categoryId = :categoryId").setParameter("categoryId", categoryId);
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public CmsCategoryModel getEntity(int modelId, int categoryId) {
        QueryHandler queryHandler = getQueryHandler("from CmsCategoryModel bean");
        queryHandler.condition("bean.modelId = :modelId").setParameter("modelId", modelId);
        queryHandler.condition("bean.categoryId = :categoryId").setParameter("categoryId", categoryId);
        return getEntity(queryHandler);
    }

    @Override
    protected CmsCategoryModel init(CmsCategoryModel entity) {
        return entity;
    }

    @Override
    protected Class<CmsCategoryModel> getEntityClass() {
        return CmsCategoryModel.class;
    }

}