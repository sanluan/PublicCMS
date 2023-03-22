package com.publiccms.views.pojo.model;

import java.util.Map;

/**
 *
 * ExtendDataParameters
 * 
 */
public class ExtendDataParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Map<String, String> extendData;

    /**
     * @return extend data
     */
    public Map<String, String> getExtendData() {
        return extendData;
    }

    /**
     * @param extendData
     */
    public void setExtendData(Map<String, String> extendData) {
        this.extendData = extendData;
    }
}