package com.publiccms.views.pojo;

import com.publiccms.entities.cms.CmsCategoryModel;

public class CmsCategoryModelParamters implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CmsCategoryModel categoryModel;
    private boolean use;

    public CmsCategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CmsCategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }
}
