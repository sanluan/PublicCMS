package com.publiccms.entities.sys;
// Generated 2016-12-6 20:53:58 by Hibernate Tools 5.1.0.Beta1

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * SysConfigDataId generated by hbm2java
 */
@Embeddable
public class SysConfigDataId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @GeneratorColumn(title = "站点", condition = true)
    @JsonIgnore
    private short siteId;
    /**
     * code<p>
     * 编码
     */
    @GeneratorColumn(title = "编码", condition = true)
    private String code;

    public SysConfigDataId() {
    }

    public SysConfigDataId(short siteId, String code) {
        this.siteId = siteId;
        this.code = code;
    }

    @Column(name = "site_id", nullable = false)
    public short getSiteId() {
        return this.siteId;
    }

    public void setSiteId(short siteId) {
        this.siteId = siteId;
    }

    @Column(name = "code", nullable = false, length = 50)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof SysConfigDataId))
            return false;
        SysConfigDataId castOther = (SysConfigDataId) other;

        return (this.getSiteId() == castOther.getSiteId()) && ((this.getCode() == castOther.getCode())
                || (this.getCode() != null && castOther.getCode() != null && this.getCode().equals(castOther.getCode())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getSiteId();
        result = 37 * result + (getCode() == null ? 0 : this.getCode().hashCode());
        return result;
    }

}
