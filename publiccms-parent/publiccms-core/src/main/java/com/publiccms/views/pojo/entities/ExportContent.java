package com.publiccms.views.pojo.entities;

import java.util.List;

import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.cms.CmsContentProduct;

public class ExportContent implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String categoryCode;
    private CmsContent content;
    private CmsContent attribute;
    private List<CmsContentFile> fileList;
    private List<CmsContentProduct> productList;
    private List<CmsContent> childList;

    /**
     * @return the categoryCode
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * @param categoryCode the categoryCode to set
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * @return the content
     */
    public CmsContent getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(CmsContent content) {
        this.content = content;
    }

    /**
     * @return the attribute
     */
    public CmsContent getAttribute() {
        return attribute;
    }

    /**
     * @param attribute
     *            the attribute to set
     */
    public void setAttribute(CmsContent attribute) {
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
     * @return the childList
     */
    public List<CmsContent> getChildList() {
        return childList;
    }

    /**
     * @param childList
     *            the childList to set
     */
    public void setChildList(List<CmsContent> childList) {
        this.childList = childList;
    }
}
