package com.publiccms.views.pojo.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * CmsPageMetadata
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsPageData implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<ExtendData> extendDataList;
    private Map<String, String> extendData;

    /**
     * 
     */
    public CmsPageData() {
    }

    /**
     * @return
     */
    @JsonIgnore
    public Map<String, String> getExtendData() {
        if (null == extendData) {
            extendData = new HashMap<>();
            if (CommonUtils.notEmpty(extendDataList)) {
                for (ExtendData extend : extendDataList) {
                    extendData.put(extend.getName(), extend.getValue());
                }
            }
        }
        return extendData;
    }

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