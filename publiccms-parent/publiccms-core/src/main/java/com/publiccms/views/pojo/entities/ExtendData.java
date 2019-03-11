package com.publiccms.views.pojo.entities;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * ExtendData
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String value;
    /**
     * 
     */
    public ExtendData() {
    }

    /**
     * @param name
     * @param value
     */
    public ExtendData(String name, String value) {
        this.name = name;
        this.value = value;
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
    public String getValue() {
        return this.value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @param values
     */
    public void setValues(String[] values) {
        this.value = StringUtils.arrayToCommaDelimitedString(values);
    }
}