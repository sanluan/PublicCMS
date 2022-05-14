package com.publiccms.entities.cms;
// Generated 2022-3-15 17:51:34 by Hibernate Tools 5.6.2.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * CmsDictionaryExcludeId generated by hbm2java
 */
@Embeddable
public class CmsDictionaryExcludeId implements java.io.Serializable {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;
    /**
     * dictionary id<p>
     * 数据字典id
     */
    @GeneratorColumn(title = "ID", condition = true)
    private String dictionaryId;
    @GeneratorColumn(title = "站点", condition = true)
    @JsonIgnore
    private short siteId;
    /**
     * exclude dictionary id<p>
     * 排除数据字典id
     */
    @GeneratorColumn(title = "排除数据字典", condition = true)
    private String excludeDictionaryId;

    public CmsDictionaryExcludeId() {
    }

    public CmsDictionaryExcludeId(String dictionaryId, short siteId, String excludeDictionaryId) {
        this.dictionaryId = dictionaryId;
        this.siteId = siteId;
        this.excludeDictionaryId = excludeDictionaryId;
    }

    @Column(name = "dictionary_id", nullable = false, length = 20)
    public String getDictionaryId() {
        return this.dictionaryId;
    }

    public void setDictionaryId(String dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    @Column(name = "site_id", nullable = false)
    public short getSiteId() {
        return this.siteId;
    }

    public void setSiteId(short siteId) {
        this.siteId = siteId;
    }

    @Column(name = "exclude_dictionary_id", nullable = false, length = 20)
    public String getExcludeDictionaryId() {
        return this.excludeDictionaryId;
    }

    public void setExcludeDictionaryId(String excludeDictionaryId) {
        this.excludeDictionaryId = excludeDictionaryId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof CmsDictionaryExcludeId))
            return false;
        CmsDictionaryExcludeId castOther = (CmsDictionaryExcludeId) other;

        return ((this.getDictionaryId() == castOther.getDictionaryId()) || (this.getDictionaryId() != null
                && castOther.getDictionaryId() != null && this.getDictionaryId().equals(castOther.getDictionaryId())))
                && (this.getSiteId() == castOther.getSiteId())
                && ((this.getExcludeDictionaryId() == castOther.getExcludeDictionaryId())
                        || (this.getExcludeDictionaryId() != null && castOther.getExcludeDictionaryId() != null
                                && this.getExcludeDictionaryId().equals(castOther.getExcludeDictionaryId())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (getDictionaryId() == null ? 0 : this.getDictionaryId().hashCode());
        result = 37 * result + this.getSiteId();
        result = 37 * result + (getExcludeDictionaryId() == null ? 0 : this.getExcludeDictionaryId().hashCode());
        return result;
    }

}
