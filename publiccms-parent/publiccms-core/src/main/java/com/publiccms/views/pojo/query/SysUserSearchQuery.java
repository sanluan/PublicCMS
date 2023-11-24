package com.publiccms.views.pojo.query;

public class SysUserSearchQuery implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Short siteId;
    private boolean projection;
    private boolean phrase;
    private String text;
    private String exclude;
    private String[] fields;
    private Integer deptId;
    private String[] extendsValues;
    private String[] dictionaryValues;
    private Boolean dictionaryUnion;

    public SysUserSearchQuery() {

    }

    public SysUserSearchQuery(Short siteId, boolean projection, boolean phrase, String text,
            String exclude, String[] fields, Integer deptId, String[] extendsValues, String[] dictionaryValues,
            Boolean dictionaryUnion) {
        super();
        this.siteId = siteId;
        this.projection = projection;
        this.phrase = phrase;
        this.text = text;
        this.exclude = exclude;
        this.fields = fields;
        this.deptId = deptId;
        this.extendsValues = extendsValues;
        this.dictionaryValues = dictionaryValues;
        this.dictionaryUnion = dictionaryUnion;
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
     * @return the projection
     */
    public boolean isProjection() {
        return projection;
    }

    /**
     * @param projection
     *            the projection to set
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
     * @param phrase
     *            the phrase to set
     */
    public void setPhrase(boolean phrase) {
        this.phrase = phrase;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
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
     * @param exclude
     *            the exclude to set
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
     * @param fields
     *            the fields to set
     */
    public void setFields(String[] fields) {
        this.fields = fields;
    }

    /**
     * @return the deptId
     */
    public Integer getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     *            the deptId to set
     */
    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    /**
     * @return the extendsValues
     */
    public String[] getExtendsValues() {
        return extendsValues;
    }

    /**
     * @param extendsValues
     *            the extendsValues to set
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
     * @param dictionaryValues
     *            the dictionaryValues to set
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
     * @param dictionaryUnion
     *            the dictionaryUnion to set
     */
    public void setDictionaryUnion(Boolean dictionaryUnion) {
        this.dictionaryUnion = dictionaryUnion;
    }

}
