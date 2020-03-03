package com.publiccms.views.pojo.entities;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.publiccms.entities.sys.SysExtendField;

/**
 *
 * CmsPlaceMetadata
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsPlaceMetadata implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String alias;
    private Integer size;
    private Long[] adminIds;
    private boolean allowContribute;
    private boolean allowAnonymous;
    private List<String> acceptItemTypes;
    private List<String> fieldList;
    private List<String> requiredFieldList;
    private Map<String,String> fieldTextMap;
    private List<SysExtendField> extendList;

    /**
     * @return
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param size
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * @return
     */
    public boolean isAllowContribute() {
        return allowContribute;
    }

    /**
     * @param allowContribute
     */
    public void setAllowContribute(boolean allowContribute) {
        this.allowContribute = allowContribute;
    }
    
    /**
     * @return
     */
    public boolean isAllowAnonymous() {
        return allowAnonymous;
    }

    /**
     * @return the acceptItemTypes
     */
    public List<String> getAcceptItemTypes() {
        return acceptItemTypes;
    }

    /**
     * @param acceptItemTypes the acceptItemTypes to set
     */
    public void setAcceptItemTypes(List<String> acceptItemTypes) {
        this.acceptItemTypes = acceptItemTypes;
    }

    /**
     * @param allowAnonymous
     */
    public void setAllowAnonymous(boolean allowAnonymous) {
        this.allowAnonymous = allowAnonymous;
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

    /**
     * @param adminIds
     */
    public void setAdminIds(Long[] adminIds) {
        this.adminIds = adminIds;
    }

    /**
     * @return
     */
    public Long[] getAdminIds() {
        return adminIds;
    }

    /**
     * @return the fieldList
     */
    public List<String> getFieldList() {
        return fieldList;
    }

    /**
     * @param fieldList the fieldList to set
     */
    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    /**
     * @return the requiredFieldList
     */
    public List<String> getRequiredFieldList() {
        return requiredFieldList;
    }

    /**
     * @param requiredFieldList the requiredFieldList to set
     */
    public void setRequiredFieldList(List<String> requiredFieldList) {
        this.requiredFieldList = requiredFieldList;
    }

    /**
     * @return the fieldTextMap
     */
    public Map<String, String> getFieldTextMap() {
        return fieldTextMap;
    }

    /**
     * @param fieldTextMap the fieldTextMap to set
     */
    public void setFieldTextMap(Map<String, String> fieldTextMap) {
        this.fieldTextMap = fieldTextMap;
    }
}