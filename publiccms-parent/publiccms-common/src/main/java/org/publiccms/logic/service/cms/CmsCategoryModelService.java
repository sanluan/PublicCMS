package org.publiccms.logic.service.cms;

import org.publiccms.entities.cms.CmsCategoryModel;
import org.publiccms.logic.dao.cms.CmsCategoryModelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

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
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(String modelId, Integer categoryId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(modelId, categoryId, pageIndex, pageSize);
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