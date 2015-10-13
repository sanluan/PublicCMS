package com.publiccms.logic.service.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import static com.sanluan.common.tools.RequestUtils.getValue;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsModel;
import com.publiccms.logic.dao.cms.CmsCategoryModelDao;
import com.publiccms.logic.dao.cms.CmsModelDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsCategoryModelService extends BaseService<CmsCategoryModel> {

    @Autowired
    private CmsCategoryModelDao dao;
    @Autowired
    private CmsModelDao modelDao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer modelId, Integer categoryId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(modelId, categoryId, pageIndex, pageSize);
    }

    public void updateCategoryModel(boolean flag, Integer categoryId, CmsModel model, Map<String, String[]> parameterMap) {
        CmsCategoryModel categoryModel = getEntity(model.getId(), categoryId);
        if (!model.isIsUrl() && !model.isIsImages()) {
            @SuppressWarnings("unchecked")
            List<CmsModel> modelList = (List<CmsModel>) modelDao
                    .getPage(model.getId(), null, null, null, null, false, null, null).getList();
            for (CmsModel child : modelList) {
                updateCategoryModel(true, categoryId, child, parameterMap);
            }
        }
        if (notEmpty(categoryModel) && flag) {
            categoryModel.setTemplatePath(getValue(parameterMap, "templatePath_" + model.getId()));
            update(categoryModel.getId(), categoryModel, new String[] { "id", "categoryId", "modelId" });
        } else if (notEmpty(categoryModel) && !flag) {
            delete(categoryModel.getId());
        } else if (null == categoryModel && flag) {
            categoryModel = new CmsCategoryModel();
            categoryModel.setCategoryId(categoryId);
            categoryModel.setModelId(model.getId());
            categoryModel.setTemplatePath(getValue(parameterMap, "templatePath_" + model.getId()));
            save(categoryModel);
        }
    }

    @Transactional(readOnly = true)
    public CmsCategoryModel getEntity(int modelId, int categoryId) {
        return dao.getEntity(modelId, categoryId);
    }
}