package org.publiccms.views.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * ExtendField
 * 
 */
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
    private String dictionaryId;

    /**
     * 
     */
    public ExtendField() {
    }

    /**
     * @param code
     * @param inputType
     * @param required
     * @param name
     * @param description
     * @param defaultValue
     */
    public ExtendField(String code, String inputType, boolean required, String name, String description, String defaultValue) {
        this.id = new ExtendFieldId(code);
        this.inputType = inputType;
        this.required = required;
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    /**
     * @return
     */
    public ExtendFieldId getId() {
        return this.id;
    }

    /**
     * @param id
     */
    public void setId(ExtendFieldId id) {
        this.id = id;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.id = new ExtendFieldId(code);
    }

    /**
     * @return
     */
    public String getInputType() {
        return this.inputType;
    }

    /**
     * @param inputType
     */
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    /**
     * @return
     */
    public boolean isRequired() {
        return this.required;
    }

    /**
     * @param required
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return
     */
    public Integer getMaxlength() {
        return maxlength;
    }

    /**
     * @param maxlength
     */
    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }

    /**
     * @return
     */
    public int getSort() {
        return sort;
    }

    /**
     * @param sort
     */
    public void setSort(int sort) {
        this.sort = sort;
    }

    /**
     * @return
     */
    public String getDictionaryId() {
        return dictionaryId;
    }

    /**
     * @param dictionaryId
     */
    public void setDictionaryId(String dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

}
