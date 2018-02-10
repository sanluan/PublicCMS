package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.views.pojo.entities.ExtendData;

/**
 *
 * CmsPlaceParamters
 * 
 */
public class CmsPlaceParamters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
	List<ExtendData> extendDataList;

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