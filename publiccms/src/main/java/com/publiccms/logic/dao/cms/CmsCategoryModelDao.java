package com.publiccms.logic.dao.cms;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsCategoryModel;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsCategoryModelDao extends BaseDao<CmsCategoryModel> {
    public PageHandler getPage(String modelId, Integer categoryId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsCategoryModel bean");
        if (notEmpty(modelId)) {
            queryHandler.condition("bean.id.modelId = :modelId").setParameter("modelId", modelId);
        }
        if (notEmpty(categoryId)) {
            queryHandler.condition("bean.id.categoryId = :categoryId").setParameter("categoryId", categoryId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsCategoryModel init(CmsCategoryModel entity) {
        return entity;
    }

}