package com.publiccms.entities.sys;
// Generated 2023-8-7 21:42:23 by Hibernate Tools 5.6.15.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SysUserCollectionId generated by hbm2java
 */
@Embeddable
public class SysUserCollectionId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private long userId;
    private String itemType;
    private String itemId;

    public SysUserCollectionId() {
    }

    public SysUserCollectionId(long userId, String itemType, String itemId) {
        this.userId = userId;
        this.itemType = itemType;
        this.itemId = itemId;
    }

    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "item_type", nullable = false, length = 50)
    public String getItemType() {
        return this.itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Column(name = "item_id", nullable = false, length = 100)
    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof SysUserCollectionId))
            return false;
        SysUserCollectionId castOther = (SysUserCollectionId) other;

        return (this.getUserId() == castOther.getUserId())
                && ((this.getItemType() == castOther.getItemType()) || (this.getItemType() != null
                        && castOther.getItemType() != null && this.getItemType().equals(castOther.getItemType())))
                && ((this.getItemId() == castOther.getItemId()) || (this.getItemId() != null && castOther.getItemId() != null
                        && this.getItemId().equals(castOther.getItemId())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (int) this.getUserId();
        result = 37 * result + (getItemType() == null ? 0 : this.getItemType().hashCode());
        result = 37 * result + (getItemId() == null ? 0 : this.getItemId().hashCode());
        return result;
    }

}
