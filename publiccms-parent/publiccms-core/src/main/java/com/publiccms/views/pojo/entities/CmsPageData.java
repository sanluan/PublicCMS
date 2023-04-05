package com.publiccms.views.pojo.entities;

import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.views.pojo.model.ExtendDataParameters;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsPageData extends ExtendDataParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public CmsPageData() {
    }

    /**
     * @param extendDataList
     */
    public void setExtendDataList(List<ExtendData> extendDataList) {
        if (CommonUtils.notEmpty(extendDataList)) {
            setExtendData(new LinkedHashMap<>());
            for (ExtendData data : extendDataList) {
                getExtendData().put(data.getName(), data.getValue());
            }
        }
    }
}

class ExtendData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String value;

    /**
     * 
     */
    public ExtendData() {
    }

    /**
     * @param name
     * @param value
     */
    public ExtendData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
}