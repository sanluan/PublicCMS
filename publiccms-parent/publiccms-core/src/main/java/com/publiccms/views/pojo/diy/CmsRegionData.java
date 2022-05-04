package com.publiccms.views.pojo.diy;

import java.util.List;

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
}
