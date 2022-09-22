package com.publiccms.views.pojo.diy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * CmsRegion diy区域
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsRegion implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * category type
     * <p>
     * 分类类型
     */
    private String categoryType;
    /**
     * name
     * <p>
     * 名称
     */
    private String name;

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
     * @return the categoryType
     */
    public String getCategoryType() {
        return categoryType;
    }

    /**
     * @param categoryType
     *            the categoryType to set
     */
    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
