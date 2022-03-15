package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.entities.cms.CmsDictionaryExclude;

/**
 *
 * CmsDictionaryExcludeParameters
 * 
 */
public class CmsDictionaryExcludeParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<CmsDictionaryExclude> excludeList;
    /**
     * @return the excludeList
     */
    public List<CmsDictionaryExclude> getExcludeList() {
        return excludeList;
    }
    /**
     * @param excludeList the excludeList to set
     */
    public void setExcludeList(List<CmsDictionaryExclude> excludeList) {
        this.excludeList = excludeList;
    }

}