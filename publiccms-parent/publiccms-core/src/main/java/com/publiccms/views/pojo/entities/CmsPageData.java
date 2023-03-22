package com.publiccms.views.pojo.entities;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.publiccms.common.tools.CommonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsPageData implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map<String, String> extendData;

    /**
     * 
     */
    public CmsPageData() {
    }

    /**
     * @return
     */
    public Map<String, String> getExtendData() {
        return extendData;
    }

    /**
     * @param extendData
     *            the extendData to set
     */
    public void setExtendData(Map<String, String> extendData) {
        this.extendData = extendData;
    }

    /**
     * @param extendDataList
     */
    public void setExtendDataList(List<ExtendData> extendDataList) {
        if (CommonUtils.notEmpty(extendDataList)) {
            extendData = new LinkedHashMap<>();
            for (ExtendData data : extendDataList) {
                extendData.put(data.getName(), data.getValue());
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

    /**
     * @param values
     */
    public void setValues(String[] values) {
        this.value = StringUtils.arrayToCommaDelimitedString(values);
    }
}