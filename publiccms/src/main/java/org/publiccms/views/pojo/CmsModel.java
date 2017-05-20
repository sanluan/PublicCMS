package org.publiccms.views.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    private boolean hasChild;
    private boolean onlyUrl;
    private boolean hasImages;
    private boolean hasFiles;
    private List<ExtendField> extendList;

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
     * @return
     */
    public List<ExtendField> getExtendList() {
        return extendList;
    }

    /**
     * @param extendList
     */
    public void setExtendList(List<ExtendField> extendList) {
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
