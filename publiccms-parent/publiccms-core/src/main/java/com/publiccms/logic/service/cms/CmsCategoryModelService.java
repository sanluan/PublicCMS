package com.publiccms.logic.service.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.logic.dao.cms.CmsCategoryModelDao;

/**
 *
 * CmsCategoryModelService
 * 
 */
@Service
@Transactional
public class CmsCategoryModelService extends BaseService<CmsCategoryModel> {

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param modelId
     * @param categoryId
     * @return results page
     */
    @Transactional(readOnly = true)
    public List<CmsCategoryModel> getList(String modelId, Integer categoryId) {
        return dao.getList(modelId, categoryId);
    }

    /**
     * @param entity
     */
    public void updateCategoryModel(CmsCategoryModel entity) {
        CmsCategoryModel oldEntity = getEntity(entity.getId());
        if (null == oldEntity) {
            save(entity);
        } else {
            update(oldEntity.getId(), entity, ignoreProperties);
        }
    }

    @Autowired
    private CmsCategoryModelDao dao;

}