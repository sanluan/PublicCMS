package com.publiccms.views.pojo;

import java.util.List;

public class SysConfigParamters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<ExtendData> extendDataList;

    public List<ExtendData> getExtendDataList() {
        return extendDataList;
    }

    public void setExtendDataList(List<ExtendData> extendDataList) {
        this.extendDataList = extendDataList;
    }

}