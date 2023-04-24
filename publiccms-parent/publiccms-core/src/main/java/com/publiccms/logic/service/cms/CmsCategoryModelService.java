package com.publiccms.logic.service.cms;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.logic.dao.cms.CmsCategoryModelDao;

/**
 *
 * CmsCategoryModelService
 * 
 */
@Service
@Transactional
public class CmsCategoryModelService extends BaseService<CmsCategoryModel> {
    private String[] ignoreProperties = new String[] { "id", "siteId" };

    /**
     * @param siteId
     * @param modelId
     * @param categoryId
     * @return results page
     */
    @Transactional(readOnly = true)
    public List<CmsCategoryModel> getList(short siteId, String modelId, Integer categoryId) {
        return dao.getList(siteId, modelId, categoryId);
    }

    public void copy(short siteId, Integer categoryId, Integer copyCategoryId) {
        List<CmsCategoryModel> list = dao.getList(siteId, null, copyCategoryId);
        if (CommonUtils.notEmpty(list)) {
            List<CmsCategoryModel> newlist = new ArrayList<>();
            for (CmsCategoryModel entity : list) {
                CmsCategoryModel e = new CmsCategoryModel();
                CmsCategoryModelId id = new CmsCategoryModelId();
                BeanUtils.copyProperties(entity, e);
                id.setCategoryId(categoryId);
                id.setModelId(entity.getId().getModelId());
                e.setId(id);
                newlist.add(e);
            }
            save(newlist);
        }
    }

    /**
     * @param siteId
     * @param entity
     */
    public void updateCategoryModel(short siteId, CmsCategoryModel entity) {
        CmsCategoryModel oldEntity = getEntity(entity.getId());
        if (null == oldEntity) {
            entity.setSiteId(siteId);
            save(entity);
        } else {
            update(oldEntity.getId(), entity, ignoreProperties);
        }
    }

    /**
     * @param siteId
     * @param modelId
     * @param categoryId
     * @return number of data deleted
     */
    public int delete(short siteId, String modelId, Integer categoryId) {
        return dao.delete(siteId, modelId, categoryId);
    }

    @Resource
    private CmsCategoryModelDao dao;
}