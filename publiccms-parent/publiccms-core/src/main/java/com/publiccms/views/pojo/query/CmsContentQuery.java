package com.publiccms.views.pojo.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CmsContentQuery implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Short siteId;
    private Integer[] status;
    private Integer categoryId;
    private Integer[] categoryIds;
    private Boolean disabled;
    private String[] modelIds;
    private Long parentId;
    private Boolean emptyParent;
    private Long quoteId;
    private Boolean emptyQuote;
    private Boolean onlyUrl;
    private Boolean hasImages;
    private Boolean hasFiles;
    private Boolean hasCover;
    private String title;
    private Long userId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startPublishDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endPublishDate;
    private Date expiryDate;

    public CmsContentQuery() {

    }

    public CmsContentQuery(Short siteId, Integer[] status, Integer categoryId, Integer[] categoryIds, Boolean disabled,
            String[] modelIds, Long parentId, Boolean emptyParent, Long quoteId, Boolean emptyQuote, Boolean onlyUrl,
            Boolean hasImages, Boolean hasFiles, Boolean hasCover, String title, Long userId, Date startPublishDate,
            Date endPublishDate, Date expiryDate) {
        super();
        this.siteId = siteId;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryIds = categoryIds;
        this.disabled = disabled;
        this.modelIds = modelIds;
        this.parentId = parentId;
        this.emptyParent = emptyParent;
        this.quoteId = quoteId;
        this.emptyQuote = emptyQuote;
        this.onlyUrl = onlyUrl;
        this.hasImages = hasImages;
        this.hasFiles = hasFiles;
        this.hasCover = hasCover;
        this.title = title;
        this.userId = userId;
        this.startPublishDate = startPublishDate;
        this.endPublishDate = endPublishDate;
        this.expiryDate = expiryDate;
    }

    /**
     * @return the siteId
     */
    public Short getSiteId() {
        return siteId;
    }

    /**
     * @param siteId
     *            the siteId to set
     */
    public void setSiteId(Short siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the status
     */
    public Integer[] getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Integer[] status) {
        this.status = status;
    }

    /**
     * @return the categoryId
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId
     *            the categoryId to set
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the categoryIds
     */
    public Integer[] getCategoryIds() {
        return categoryIds;
    }

    /**
     * @param categoryIds
     *            the categoryIds to set
     */
    public void setCategoryIds(Integer[] categoryIds) {
        this.categoryIds = categoryIds;
    }

    /**
     * @return the disabled
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * @param disabled
     *            the disabled to set
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the modelIds
     */
    public String[] getModelIds() {
        return modelIds;
    }

    /**
     * @param modelIds
     *            the modelIds to set
     */
    public void setModelIds(String[] modelIds) {
        this.modelIds = modelIds;
    }

    /**
     * @return the parentId
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the emptyParent
     */
    public Boolean getEmptyParent() {
        return emptyParent;
    }

    /**
     * @param emptyParent
     *            the emptyParent to set
     */
    public void setEmptyParent(Boolean emptyParent) {
        this.emptyParent = emptyParent;
    }

    /**
     * @return the quote
     */
    public Long getQuoteId() {
        return quoteId;
    }

    /**
     * @param quoteId
     *            the quoteId to set
     */
    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    /**
     * @return the emptyQuote
     */
    public Boolean getEmptyQuote() {
        return emptyQuote;
    }

    /**
     * @param emptyQuote
     *            the emptyQuote to set
     */
    public void setEmptyQuote(Boolean emptyQuote) {
        this.emptyQuote = emptyQuote;
    }

    /**
     * @return the onlyUrl
     */
    public Boolean getOnlyUrl() {
        return onlyUrl;
    }

    /**
     * @param onlyUrl
     *            the onlyUrl to set
     */
    public void setOnlyUrl(Boolean onlyUrl) {
        this.onlyUrl = onlyUrl;
    }

    /**
     * @return the hasImages
     */
    public Boolean getHasImages() {
        return hasImages;
    }

    /**
     * @param hasImages
     *            the hasImages to set
     */
    public void setHasImages(Boolean hasImages) {
        this.hasImages = hasImages;
    }

    /**
     * @return the hasFiles
     */
    public Boolean getHasFiles() {
        return hasFiles;
    }

    /**
     * @param hasFiles
     *            the hasFiles to set
     */
    public void setHasFiles(Boolean hasFiles) {
        this.hasFiles = hasFiles;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the startPublishDate
     */
    public Date getStartPublishDate() {
        return startPublishDate;
    }

    /**
     * @param startPublishDate
     *            the startPublishDate to set
     */
    public void setStartPublishDate(Date startPublishDate) {
        this.startPublishDate = startPublishDate;
    }

    /**
     * @return the endPublishDate
     */
    public Date getEndPublishDate() {
        return endPublishDate;
    }

    /**
     * @param endPublishDate
     *            the endPublishDate to set
     */
    public void setEndPublishDate(Date endPublishDate) {
        this.endPublishDate = endPublishDate;
    }

    /**
     * @return the hasCover
     */
    public Boolean getHasCover() {
        return hasCover;
    }

    /**
     * @param hasCover
     */
    public void setHasCover(Boolean hasCover) {
        this.hasCover = hasCover;
    }

    /**
     * @return the expiryDate
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate
     *            the expiryDate to set
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
