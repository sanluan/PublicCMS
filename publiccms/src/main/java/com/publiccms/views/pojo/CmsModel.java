package com.publiccms.views.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    private boolean hasChild;
    private boolean onlyUrl;
    private boolean hasImages;
    private boolean hasFiles;
    private List<ExtendField> extendList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public boolean isOnlyUrl() {
        return onlyUrl;
    }

    public void setOnlyUrl(boolean onlyUrl) {
        this.onlyUrl = onlyUrl;
    }

    public boolean isHasImages() {
        return hasImages;
    }

    public void setHasImages(boolean hasImages) {
        this.hasImages = hasImages;
    }

    public boolean isHasFiles() {
        return hasFiles;
    }

    public void setHasFiles(boolean hasFiles) {
        this.hasFiles = hasFiles;
    }

    public List<ExtendField> getExtendList() {
        return extendList;
    }

    public void setExtendList(List<ExtendField> extendList) {
        this.extendList = extendList;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
