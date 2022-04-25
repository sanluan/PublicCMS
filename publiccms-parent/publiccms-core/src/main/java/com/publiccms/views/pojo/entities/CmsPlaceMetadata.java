package com.publiccms.views.pojo.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer coverWidth;
    private Integer coverHeight;
    private List<String> acceptItemTypes;
    private List<String> fieldList;
    private List<String> requiredFieldList;
    private Map<String, String> fieldTextMap;
    private List<SysExtendField> extendList;
    private List<SysExtendField> metadataExtendList;

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
     * @param allowAnonymous
     */
    public void setAllowAnonymous(boolean allowAnonymous) {
        this.allowAnonymous = allowAnonymous;
    }

    /**
     * @return the coverWidth
     */
    public Integer getCoverWidth() {
        return coverWidth;
    }

    /**
     * @param coverWidth
     *            the coverWidth to set
     */
    public void setCoverWidth(Integer coverWidth) {
        this.coverWidth = coverWidth;
    }

    /**
     * @return the coverHeight
     */
    public Integer getCoverHeight() {
        return coverHeight;
    }

    /**
     * @param coverHeight
     *            the coverHeight to set
     */
    public void setCoverHeight(Integer coverHeight) {
        this.coverHeight = coverHeight;
    }

    /**
     * @return the acceptItemTypes
     */
    public List<String> getAcceptItemTypes() {
        return acceptItemTypes;
    }

    /**
     * @param acceptItemTypes
     *            the acceptItemTypes to set
     */
    public void setAcceptItemTypes(List<String> acceptItemTypes) {
        this.acceptItemTypes = acceptItemTypes;
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
     * @param fieldList
     *            the fieldList to set
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
     * @param requiredFieldList
     *            the requiredFieldList to set
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
     * @param fieldTextMap
     *            the fieldTextMap to set
     */
    public void setFieldTextMap(Map<String, String> fieldTextMap) {
        this.fieldTextMap = fieldTextMap;
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
     * @return the metadataExtendList
     */
    public List<SysExtendField> getMetadataExtendList() {
        return metadataExtendList;
    }

    /**
     * @param metadataExtendList
     *            the metadataExtendList to set
     */
    public void setMetadataExtendList(List<SysExtendField> metadataExtendList) {
        this.metadataExtendList = metadataExtendList;
    }

    @JsonIgnore
    public Map<String, Object> getAsMap(CmsPageData data) {
        Map<String, Object> map = new HashMap<>();
        map.put("alias", getAlias());
        map.put("size", getSize());
        map.put("allowContribute", isAllowContribute());
        map.put("allowAnonymous", isAllowAnonymous());
        map.put("coverWidth", getCoverWidth());
        map.put("coverHeight", getCoverHeight());
        map.put("acceptItemTypes", getAcceptItemTypes());
        map.put("extendList", getExtendList());
        map.put("adminIds", getAdminIds());
        map.put("extendData", data.getExtendData());
        map.put("fieldList", getFieldList());
        map.put("requiredFieldList", getRequiredFieldList());
        map.put("fieldTextMap", getFieldTextMap());
        map.put("metadataExtendList", getMetadataExtendList());
        return map;
    }
}