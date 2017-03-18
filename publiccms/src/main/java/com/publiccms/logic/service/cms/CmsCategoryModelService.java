package com.publiccms.logic.service.cms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.logic.dao.cms.CmsCategoryModelDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsCategoryModelService extends BaseService<CmsCategoryModel> {
    private String[] ignoreProperties = new String[] { "id" };

    @Transactional(readOnly = true)
    public PageHandler getPage(String modelId, Integer categoryId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(modelId, categoryId, pageIndex, pageSize);
    }

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