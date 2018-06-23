package com.publiccms.views.pojo.model;

import com.publiccms.entities.cms.CmsCategoryModel;

/**
 *
 * CmsCategoryModelParameters
 * 
 */
public class CmsCategoryModelParameters implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CmsCategoryModel categoryModel;
    private boolean use;

    /**
     * @return
     */
    public CmsCategoryModel getCategoryModel() {
        return categoryModel;
    }

    /**
     * @param categoryModel
     */
    public void setCategoryModel(CmsCategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    /**
     * @return
     */
    public boolean isUse() {
        return use;
    }

    /**
     * @param use
     */
    public void setUse(boolean use) {
        this.use = use;
    }
}
