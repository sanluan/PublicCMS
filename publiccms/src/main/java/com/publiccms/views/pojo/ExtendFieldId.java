package com.publiccms.views.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendFieldId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String code;

    public ExtendFieldId() {
    }

    public ExtendFieldId(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
