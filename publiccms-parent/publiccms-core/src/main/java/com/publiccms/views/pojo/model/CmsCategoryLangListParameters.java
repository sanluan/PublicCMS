package com.publiccms.views.pojo.model;

import java.util.List;

/**
 *
 * CmsCategoryLangListParameters
 * 
 */
public class CmsCategoryLangListParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<CmsCategoryLangParameters> categoryLangList;

    /**
     * @return the categoryLangList
     */
    public List<CmsCategoryLangParameters> getCategoryLangList() {
        return categoryLangList;
    }

    /**
     * @param categoryLangList
     *            the categoryLangList to set
     */
    public void setCategoryLangList(List<CmsCategoryLangParameters> categoryLangList) {
        this.categoryLangList = categoryLangList;
    }
}