package com.publiccms.views.pojo.entities;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.publiccms.entities.sys.SysExtendField;

/**
 *
 * CmsModel
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsModel implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String parentId;
    private String name;
    private String templatePath;
    private String editorType;
    private boolean hasChild;
    private boolean onlyUrl;
    private boolean hasImages;
    private boolean hasFiles;
    private boolean searchable;
    private List<String> fieldList;
    private List<String> requiredFieldList;
    private Map<String, String> fieldTextMap;
    private List<SysExtendField> extendList;

    /**
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
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
    public String getTemplatePath() {
        return templatePath;
    }

    /**
     * @param templatePath
     */
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    /**
     * @return
     */
    public String getEditorType() {
        return editorType;
    }

    /**
     * @param editorType
     */
    public void setEditorType(String editorType) {
        this.editorType = editorType;
    }

    /**
     * @return
     */
    public boolean isHasChild() {
        return hasChild;
    }

    /**
     * @param hasChild
     */
    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    /**
     * @return
     */
    public boolean isOnlyUrl() {
        return onlyUrl;
    }

    /**
     * @param onlyUrl
     */
    public void setOnlyUrl(boolean onlyUrl) {
        this.onlyUrl = onlyUrl;
    }

    /**
     * @return
     */
    public boolean isHasImages() {
        return hasImages;
    }

    /**
     * @param hasImages
     */
    public void setHasImages(boolean hasImages) {
        this.hasImages = hasImages;
    }

    /**
     * @return
     */
    public boolean isHasFiles() {
        return hasFiles;
    }

    /**
     * @param hasFiles
     */
    public void setHasFiles(boolean hasFiles) {
        this.hasFiles = hasFiles;
    }

    /**
     * @return the searchable
     */
    public boolean isSearchable() {
        return searchable;
    }

    /**
     * @param searchable
     *            the searchable to set
     */
    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
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
     * @return
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
