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
    /**
     * id
     */
    private String id;
    /**
     * parent id
     * <p>
     * 父id
     */
    private String parentId;
    /**
     * name
     * <p>
     * 名称
     * 
     */
    private String name;
    /**
     * static template path
     * <p>
     * 静态化模型路径
     * 
     */
    private String templatePath;
    /**
     * editor type
     * <p>
     * 编辑器类型
     */
    private String editorType;
    /**
     * cover width
     * <p>
     * 封面图宽度
     */
    private Integer coverWidth;
    /**
     * cover height
     * <p>
     * 封面图高度
     */
    private Integer coverHeight;
    /**
     * has child
     * <p>
     * 外链
     */
    private boolean hasChild;
    /**
     * extend link
     * <p>
     */
    private boolean onlyUrl;
    /**
     * has image list
     * <p>
     * 有用图片列表
     */
    private boolean hasImages;
    /**
     * has file list
     * <p>
     * 拥有文件列表
     * 
     */
    private boolean hasFiles;
    /**
     * has product list
     * <p>
     * 拥有图片列表
     */
    private boolean hasProducts;
    /**
     * searchable
     * <p>
     * 可搜索
     */
    private boolean searchable;
    /**
     * field list
     * <p>
     * 字段列表
     */
    private List<String> fieldList;
    /**
     * required field list
     * <p>
     * 必填字段列表
     */
    private List<String> requiredFieldList;
    /**
     * field text map
     * <p>
     * 字段文本
     */
    private Map<String, String> fieldTextMap;
    /**
     * extend field list
     * <p>
     * 扩展字段列表
     */
    private List<SysExtendField> extendList;
    /**
     * related list
     * <p>
     * 推荐列表
     */
    private List<ContentRelated> relatedList;

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
     * @return the hasProducts
     */
    public boolean isHasProducts() {
        return hasProducts;
    }

    /**
     * @param hasProducts
     *            the hasProducts to set
     */
    public void setHasProducts(boolean hasProducts) {
        this.hasProducts = hasProducts;
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

    /**
     * @return the relatedList
     */
    public List<ContentRelated> getRelatedList() {
        return relatedList;
    }

    /**
     * @param relatedList the relatedList to set
     */
    public void setRelatedList(List<ContentRelated> relatedList) {
        this.relatedList = relatedList;
    }
}
