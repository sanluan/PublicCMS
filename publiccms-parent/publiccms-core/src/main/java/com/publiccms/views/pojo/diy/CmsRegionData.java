package com.publiccms.views.pojo.diy;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * CmsRegionData diy区域数据
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsRegionData implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private List<CmsLayoutData> layoutList;
    private Map<Integer, List<CmsLayoutData>> categoryLayoutMap;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the layoutList
     */
    public List<CmsLayoutData> getLayoutList() {
        return layoutList;
    }

    /**
     * @param layoutList
     *            the layoutList to set
     */
    public void setLayoutList(List<CmsLayoutData> layoutList) {
        this.layoutList = layoutList;
    }

    /**
     * @return the categoryLayoutMap
     */
    public Map<Integer, List<CmsLayoutData>> getCategoryLayoutMap() {
        return categoryLayoutMap;
    }

    /**
     * @param categoryLayoutMap the categoryLayoutMap to set
     */
    public void setCategoryLayoutMap(Map<Integer, List<CmsLayoutData>> categoryLayoutMap) {
        this.categoryLayoutMap = categoryLayoutMap;
    }
}
