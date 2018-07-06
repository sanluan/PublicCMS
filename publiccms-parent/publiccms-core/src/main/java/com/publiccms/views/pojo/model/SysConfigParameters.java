package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.views.pojo.entities.ExtendData;

/**
 *
 * SysConfigParameters
 * 
 */
public class SysConfigParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<ExtendData> extendDataList;

    /**
     * @return
     */
    public List<ExtendData> getExtendDataList() {
        return extendDataList;
    }

    /**
     * @param extendDataList
     */
    public void setExtendDataList(List<ExtendData> extendDataList) {
        this.extendDataList = extendDataList;
    }

}