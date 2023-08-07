package com.publiccms.entities.sys;
// Generated 2023-8-7 21:42:23 by Hibernate Tools 5.6.15.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SysUserCertificationId generated by hbm2java
 */
@Embeddable
public class SysUserCertificationId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private long userId;
    private int certificationId;

    public SysUserCertificationId() {
    }

    public SysUserCertificationId(long userId, int certificationId) {
        this.userId = userId;
        this.certificationId = certificationId;
    }

    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "certification_id", nullable = false)
    public int getCertificationId() {
        return this.certificationId;
    }

    public void setCertificationId(int certificationId) {
        this.certificationId = certificationId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof SysUserCertificationId))
            return false;
        SysUserCertificationId castOther = (SysUserCertificationId) other;

        return (this.getUserId() == castOther.getUserId()) && (this.getCertificationId() == castOther.getCertificationId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (int) this.getUserId();
        result = 37 * result + this.getCertificationId();
        return result;
    }

}
