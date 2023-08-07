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
 * SysUserCollection generated by hbm2java
 */
@Entity
@Table(name = "sys_user_collection")
public class SysUserCollection implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private SysUserCollectionId id;
    private Date createDate;

    public SysUserCollection() {
    }

    public SysUserCollection(SysUserCollectionId id, Date createDate) {
        this.id = id;
        this.createDate = createDate;
    }

    @EmbeddedId

    @AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)),
            @AttributeOverride(name = "itemType", column = @Column(name = "item_type", nullable = false, length = 50)),
            @AttributeOverride(name = "itemId", column = @Column(name = "item_id", nullable = false, length = 100)) })
    public SysUserCollectionId getId() {
        return this.id;
    }

    public void setId(SysUserCollectionId id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, length = 19)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
