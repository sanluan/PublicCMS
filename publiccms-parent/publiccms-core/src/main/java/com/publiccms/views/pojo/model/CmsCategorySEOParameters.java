package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.entities.cms.CmsCategoryAttribute;

/**
 *
 * CmsCategorySEOParameters
 * 
 */
public class CmsCategorySEOParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<CmsCategoryAttribute> attributeList;

    /**
     * @return the attributeList
     */
    public List<CmsCategoryAttribute> getAttributeList() {
        return attributeList;
    }

    /**
     * @param attributeList
     *            the attributeList to set
     */
    public void setAttributeList(List<CmsCategoryAttribute> attributeList) {
        this.attributeList = attributeList;
    }
}