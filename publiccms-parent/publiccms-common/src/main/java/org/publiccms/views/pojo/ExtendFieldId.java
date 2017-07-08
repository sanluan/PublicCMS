package org.publiccms.views.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * ExtendFieldId
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendFieldId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String code;

    /**
     * 
     */
    public ExtendFieldId() {
    }

    /**
     * @param code
     */
    public ExtendFieldId(String code) {
        this.code = code;
    }

    /**
     * @return
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

}
