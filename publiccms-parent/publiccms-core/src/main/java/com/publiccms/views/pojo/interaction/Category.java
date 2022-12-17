package com.publiccms.views.pojo.interaction;

import java.util.List;

import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.sys.SysExtendField;

public class Category implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String parentCode;
    private CmsCategory entity;
    private List<String> tagTypeList;
    private CmsCategoryAttribute attribute;
    private List<CmsCategoryModel> modelList;
    private List<SysExtendField> extendList;
    private List<Category> childList;

    /**
     * @return the parentCode
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * @param parentCode
     *            the parentCode to set
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * @return the entity
     */
    public CmsCategory getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(CmsCategory entity) {
        this.entity = entity;
    }

    /**
     * @return the tagTypeList
     */
    public List<String> getTagTypeList() {
        return tagTypeList;
    }

    /**
     * @param tagTypeList the tagTypeList to set
     */
    public void setTagTypeList(List<String> tagTypeList) {
        this.tagTypeList = tagTypeList;
    }

    /**
     * @return the attribute
     */
    public CmsCategoryAttribute getAttribute() {
        return attribute;
    }

    /**
     * @param attribute
     *            the attribute to set
     */
    public void setAttribute(CmsCategoryAttribute attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the modelList
     */
    public List<CmsCategoryModel> getModelList() {
        return modelList;
    }

    /**
     * @param modelList
     *            the modelList to set
     */
    public void setModelList(List<CmsCategoryModel> modelList) {
        this.modelList = modelList;
    }

    /**
     * @return the extendList
     */
    public List<SysExtendField> getExtendList() {
        return extendList;
    }

    /**
     * @param extendList
     *            the extendList to set
     */
    public void setExtendList(List<SysExtendField> extendList) {
        this.extendList = extendList;
    }

    /**
     * @return the childList
     */
    public List<Category> getChildList() {
        return childList;
    }

    /**
     * @param childList
     *            the childList to set
     */
    public void setChildList(List<Category> childList) {
        this.childList = childList;
    }
}
