package com.publiccms.views.pojo.diy;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * CmsDiyData diy数据
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsDiyData implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String path;
    private Map<String, CmsRegionData> regionMap;

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the regionMap
     */
    public Map<String, CmsRegionData> getRegionMap() {
        return regionMap;
    }

    /**
     * @param regionMap
     *            the regionMap to set
     */
    public void setRegionMap(Map<String, CmsRegionData> regionMap) {
        this.regionMap = regionMap;
    }
}
