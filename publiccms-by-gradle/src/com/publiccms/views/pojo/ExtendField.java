package com.publiccms.views.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendField implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean required;
    private String name;
    private String description;
    private String code;
    private String inputType;
    private String defaultValue;

    public ExtendField() {
    }

    public ExtendField(boolean required, String name, String description, String code, String inputType, String defaultValue) {
        this.required = required;
        this.name = name;
        this.description = description;
        this.code = code;
        this.inputType = inputType;
        this.defaultValue = defaultValue;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInputType() {
        return this.inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
