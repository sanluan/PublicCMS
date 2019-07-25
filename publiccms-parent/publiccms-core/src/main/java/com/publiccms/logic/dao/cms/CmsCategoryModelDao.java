package com.publiccms.logic.dao.cms;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategoryModel;

/**
 *
 * CmsCategoryModelDao
 * 
 */
@Repository
public class CmsCategoryModelDao extends BaseDao<CmsCategoryModel> {

    /**
     * @param modelId
     * @param categoryId
     * @return results list
     */
    public List<CmsCategoryModel> getList(String modelId, Integer categoryId) {
        QueryHandler queryHandler = getQueryHandler("from CmsCategoryModel bean");
        if (CommonUtils.notEmpty(modelId)) {
            queryHandler.condition("bean.id.modelId = :modelId").setParameter("modelId", modelId);
        }
        if (CommonUtils.notEmpty(categoryId)) {
            queryHandler.condition("bean.id.categoryId = :categoryId").setParameter("categoryId", categoryId);
        }
        queryHandler.order("bean.id desc");
        return getEntityList(queryHandler);
    }

    @Override
    protected CmsCategoryModel init(CmsCategoryModel entity) {
        return entity;
    }

}