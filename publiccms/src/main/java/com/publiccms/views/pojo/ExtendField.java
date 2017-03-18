package com.publiccms.views.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendField implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ExtendFieldId id;
    private boolean required;
    private Integer maxlength;
    private int sort;
    private String name;
    private String description;
    private String inputType;
    private String defaultValue;
    private String dictionaryType;
    private String dictionaryId;

    public ExtendField() {
    }

    public ExtendField(String code, String inputType, boolean required, String name, String description, String defaultValue) {
        this.id = new ExtendFieldId(code);
        this.inputType = inputType;
        this.required = required;
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public ExtendFieldId getId() {
        return this.id;
    }

    public void setId(ExtendFieldId id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.id = new ExtendFieldId(code);
    }

    public String getInputType() {
        return this.inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
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

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getDictionaryType() {
        return dictionaryType;
    }

    public void setDictionaryType(String dictionaryType) {
        this.dictionaryType = dictionaryType;
    }

    public String getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(String dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

}
