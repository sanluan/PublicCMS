package com.publiccms.views.pojo.query;

public class CmsCategoryQuery implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Short siteId;
    private Integer parentId;
    private Boolean queryAll;
    private Integer typeId;
    private Boolean allowContribute;
    private Boolean hidden;
    private Boolean disabled;
    
    public CmsCategoryQuery(){
    }

    public CmsCategoryQuery(Short siteId, Integer parentId, Boolean queryAll, Integer typeId, Boolean allowContribute,
            Boolean hidden, Boolean disabled) {
        this.siteId = siteId;
        this.parentId = parentId;
        this.queryAll = queryAll;
        this.typeId = typeId;
        this.allowContribute = allowContribute;
        this.hidden = hidden;
        this.disabled = disabled;
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
     * @return the parentId
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the queryAll
     */
    public Boolean getQueryAll() {
        return queryAll;
    }

    /**
     * @param queryAll the queryAll to set
     */
    public void setQueryAll(Boolean queryAll) {
        this.queryAll = queryAll;
    }

    /**
     * @return the typeId
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the allowContribute
     */
    public Boolean getAllowContribute() {
        return allowContribute;
    }

    /**
     * @param allowContribute the allowContribute to set
     */
    public void setAllowContribute(Boolean allowContribute) {
        this.allowContribute = allowContribute;
    }

    /**
     * @return the hidden
     */
    public Boolean getHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * @return the disabled
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

}
