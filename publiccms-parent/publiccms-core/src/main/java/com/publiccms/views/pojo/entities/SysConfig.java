package com.publiccms.views.pojo.entities;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.publiccms.entities.sys.SysExtendField;

import jakarta.validation.constraints.NotNull;

/**
 *
 * SysConfig
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysConfig implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @NotNull
    @Length(max = 50)
    private String code;
    @Length(max = 300)
    private String description;
    private List<SysExtendField> extendList;

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

    /**
     * @return
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
     * @return
     */
    public List<SysExtendField> getExtendList() {
        return extendList;
    }

    /**
     * @param extendList
     */
    public void setExtendList(List<SysExtendField> extendList) {
        this.extendList = extendList;
    }
}
