package com.publiccms.views.pojo.entities;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

public class ConfigInfo implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * code
     * <p>
     * 编码
     */
    @NotNull
    @Length(max = 50)
    private String code;
    /**
     * description
     * <p>
     * 描述
     */
    @Length(max = 300)
    private String description;
    /**
     * customed
     * <p>
     * 自定义
     */
    private boolean customed;

    /**
     * @param code
     * @param description
     */
    public ConfigInfo(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return customed
     */
    public boolean isCustomed() {
        return customed;
    }

    /**
     * @param customed
     */
    public void setCustomed(boolean customed) {
        this.customed = customed;
    }
}