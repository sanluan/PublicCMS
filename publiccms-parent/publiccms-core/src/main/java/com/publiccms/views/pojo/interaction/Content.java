package com.publiccms.views.pojo.interaction;

import java.util.List;

import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.cms.CmsContentProduct;
import com.publiccms.entities.cms.CmsContentRelated;

public class Content implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String categoryCode;
    private CmsContent entity;
    private CmsContentAttribute attribute;
    private List<CmsContentFile> fileList;
    private List<CmsContentProduct> productList;
    private List<CmsContentRelated> relatedList;
    private List<Content> childList;

    /**
     * @return the categoryCode
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * @param categoryCode
     *            the categoryCode to set
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * @return the entity
     */
    public CmsContent getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(CmsContent entity) {
        this.entity = entity;
    }

    /**
     * @return the attribute
     */
    public CmsContentAttribute getAttribute() {
        return attribute;
    }

    /**
     * @param attribute
     *            the attribute to set
     */
    public void setAttribute(CmsContentAttribute attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the fileList
     */
    public List<CmsContentFile> getFileList() {
        return fileList;
    }

    /**
     * @param fileList
     *            the fileList to set
     */
    public void setFileList(List<CmsContentFile> fileList) {
        this.fileList = fileList;
    }

    /**
     * @return the productList
     */
    public List<CmsContentProduct> getProductList() {
        return productList;
    }

    /**
     * @param productList
     *            the productList to set
     */
    public void setProductList(List<CmsContentProduct> productList) {
        this.productList = productList;
    }

    /**
     * @return the relatedList
     */
    public List<CmsContentRelated> getRelatedList() {
        return relatedList;
    }

    /**
     * @param relatedList
     *            the relatedList to set
     */
    public void setRelatedList(List<CmsContentRelated> relatedList) {
        this.relatedList = relatedList;
    }

    /**
     * @return the childList
     */
    public List<Content> getChildList() {
        return childList;
    }

    /**
     * @param childList
     *            the childList to set
     */
    public void setChildList(List<Content> childList) {
        this.childList = childList;
    }
}
