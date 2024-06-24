package com.publiccms.views.pojo.diy;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;

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
    /**
     * id
     */
    private String id;
    /**
     * name
     * <p>
     * 名称
     */
    @NotNull
    @Length(max = 50)
    private String name;
    /**
     * place path
     * <p>
     * 页面片段路径
     */
    private String place;
    /**
     * fragment path
     * <p>
     * 模板片段路径
     */
    private String fragment;

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

    /**
     * @return the fragment
     */
    public String getFragment() {
        return fragment;
    }

    /**
     * @param fragment the fragment to set
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

}
