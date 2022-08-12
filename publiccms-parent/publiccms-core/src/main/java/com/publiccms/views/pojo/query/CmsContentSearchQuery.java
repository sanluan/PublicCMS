package com.publiccms.views.pojo.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.publiccms.common.base.HighLighterQuery;

public class CmsContentSearchQuery implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Short siteId;
    private boolean projection;
    private boolean phrase;
    private HighLighterQuery highLighterQuery;
    private String text;
    private String exclude;
    private String[] fields;
    private Long[] tagIds;
    private Integer categoryId;
    private Integer[] categoryIds;
    private String[] modelIds;
    private String[] extendsValues;
    private String[] dictionaryValues;
    private Boolean dictionaryUnion;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startPublishDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endPublishDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    public CmsContentSearchQuery() {

    }
    
    public CmsContentSearchQuery(Short siteId, boolean projection, boolean phrase, HighLighterQuery highLighterQuery, String text,
            String exclude, String[] fields, Long[] tagIds, Integer categoryId, Integer[] categoryIds, String[] modelIds,
            String[] extendsValues, String[] dictionaryValues, Boolean dictionaryUnion, Date startPublishDate,
            Date endPublishDate, Date expiryDate) {
        super();
        this.siteId = siteId;
        this.projection = projection;
        this.phrase = phrase;
        this.highLighterQuery = highLighterQuery;
        this.text = text;
        this.exclude = exclude;
        this.fields = fields;
        this.tagIds = tagIds;
        this.categoryId = categoryId;
        this.categoryIds = categoryIds;
        this.modelIds = modelIds;
        this.extendsValues = extendsValues;
        this.dictionaryValues = dictionaryValues;
        this.dictionaryUnion = dictionaryUnion;
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
     * @param siteId the siteId to set
     */
    public void setSiteId(Short siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the projection
     */
    public boolean isProjection() {
        return projection;
    }

    /**
     * @param projection the projection to set
     */
    public void setProjection(boolean projection) {
        this.projection = projection;
    }

    /**
     * @return the phrase
     */
    public boolean isPhrase() {
        return phrase;
    }

    /**
     * @param phrase the phrase to set
     */
    public void setPhrase(boolean phrase) {
        this.phrase = phrase;
    }

    /**
     * @return the highLighterQuery
     */
    public HighLighterQuery getHighLighterQuery() {
        return highLighterQuery;
    }

    /**
     * @param highLighterQuery the highLighterQuery to set
     */
    public void setHighLighterQuery(HighLighterQuery highLighterQuery) {
        this.highLighterQuery = highLighterQuery;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the exclude
     */
    public String getExclude() {
        return exclude;
    }

    /**
     * @param exclude the exclude to set
     */
    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    /**
     * @return the fields
     */
    public String[] getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(String[] fields) {
        this.fields = fields;
    }

    /**
     * @return the tagIds
     */
    public Long[] getTagIds() {
        return tagIds;
    }

    /**
     * @param tagIds the tagIds to set
     */
    public void setTagIds(Long[] tagIds) {
        this.tagIds = tagIds;
    }

    /**
     * @return the categoryId
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
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
     * @param categoryIds the categoryIds to set
     */
    public void setCategoryIds(Integer[] categoryIds) {
        this.categoryIds = categoryIds;
    }

    /**
     * @return the modelIds
     */
    public String[] getModelIds() {
        return modelIds;
    }

    /**
     * @param modelIds the modelIds to set
     */
    public void setModelIds(String[] modelIds) {
        this.modelIds = modelIds;
    }

    /**
     * @return the extendsValues
     */
    public String[] getExtendsValues() {
        return extendsValues;
    }

    /**
     * @param extendsValues the extendsValues to set
     */
    public void setExtendsValues(String[] extendsValues) {
        this.extendsValues = extendsValues;
    }

    /**
     * @return the dictionaryValues
     */
    public String[] getDictionaryValues() {
        return dictionaryValues;
    }

    /**
     * @param dictionaryValues the dictionaryValues to set
     */
    public void setDictionaryValues(String[] dictionaryValues) {
        this.dictionaryValues = dictionaryValues;
    }

    /**
     * @return the dictionaryUnion
     */
    public Boolean getDictionaryUnion() {
        return dictionaryUnion;
    }

    /**
     * @param dictionaryUnion the dictionaryUnion to set
     */
    public void setDictionaryUnion(Boolean dictionaryUnion) {
        this.dictionaryUnion = dictionaryUnion;
    }

    /**
     * @return the startPublishDate
     */
    public Date getStartPublishDate() {
        return startPublishDate;
    }

    /**
     * @param startPublishDate the startPublishDate to set
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
     * @param endPublishDate the endPublishDate to set
     */
    public void setEndPublishDate(Date endPublishDate) {
        this.endPublishDate = endPublishDate;
    }

    /**
     * @return the expiryDate
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
