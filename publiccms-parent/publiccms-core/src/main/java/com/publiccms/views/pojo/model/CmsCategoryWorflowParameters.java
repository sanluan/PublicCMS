package com.publiccms.views.pojo.model;

import java.util.List;

/**
 *
 * CmsCategoryWorflowParameters
 * 
 */
public class CmsCategoryWorflowParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<Integer> categoryIdList;

    /**
     * @return the categoryIdList
     */
    public List<Integer> getCategoryIdList() {
        return categoryIdList;
    }

    /**
     * @param categoryIdList
     *            the categoryIdList to set
     */
    public void setCategoryIdList(List<Integer> categoryIdList) {
        this.categoryIdList = categoryIdList;
    }
}