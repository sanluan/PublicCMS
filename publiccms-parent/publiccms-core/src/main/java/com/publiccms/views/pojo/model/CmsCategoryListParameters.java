package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.entities.cms.CmsCategory;

/**
 *
 * CmsCategoryListParameters
 * 
 */
public class CmsCategoryListParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<CmsCategory> categoryList;

    /**
     * @return the categoryList
     */
    public List<CmsCategory> getCategoryList() {
        return categoryList;
    }

    /**
     * @param categoryList
     *            the categoryList to set
     */
    public void setCategoryList(List<CmsCategory> categoryList) {
        this.categoryList = categoryList;
    }
}