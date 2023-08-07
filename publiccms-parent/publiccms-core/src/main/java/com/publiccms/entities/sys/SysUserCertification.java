package com.publiccms.entities.sys;
// Generated 2023-8-7 21:42:23 by Hibernate Tools 5.6.15.Final

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysUserCertification generated by hbm2java
 */
@Entity
@Table(name = "sys_user_certification")
public class SysUserCertification implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private SysUserCertificationId id;
    private int status;
    private String reason;
    private Date createDate;
    private Date expiryDate;

    public SysUserCertification() {
    }

    public SysUserCertification(SysUserCertificationId id, int status, Date createDate) {
        this.id = id;
        this.status = status;
        this.createDate = createDate;
    }

    public SysUserCertification(SysUserCertificationId id, int status, String reason, Date createDate, Date expiryDate) {
        this.id = id;
        this.status = status;
        this.reason = reason;
        this.createDate = createDate;
        this.expiryDate = expiryDate;
    }

    @EmbeddedId

    @AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)),
            @AttributeOverride(name = "certificationId", column = @Column(name = "certification_id", nullable = false)) })
    public SysUserCertificationId getId() {
        return this.id;
    }

    public void setId(SysUserCertificationId id) {
        this.id = id;
    }

    @Column(name = "status", nullable = false)
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "reason")
    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, length = 19)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", length = 19)
    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

}
