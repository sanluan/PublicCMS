package com.publiccms.views.pojo.diy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * CmsModuleData diy组件数据
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsModuleData implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String place;

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

    /**
     * @return the place
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param place
     *            the place to set
     */
    public void setPlace(String place) {
        this.place = place;
    }

}
