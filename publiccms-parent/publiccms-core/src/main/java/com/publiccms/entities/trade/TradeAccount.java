package com.publiccms.entities.trade;
// Generated 2019-6-16 9:09:11 by Hibernate Tools 6.0.0-SNAPSHOT

import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * TradeAccount generated by hbm2java
 */
@Entity
@Table(name = "trade_account")
public class TradeAccount implements java.io.Serializable {

    /**
    *
    */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @GeneratorColumn(title = "用户")
    private long id;
    @GeneratorColumn(title = "站点", condition = true)
    @JsonIgnore
    private short siteId;
    /**
     * amount<p>
     * 金额
     */
    @GeneratorColumn(title = "金额")
    private BigDecimal amount;
    /**
     * update date<p>
     * 更新日期
     */
    @GeneratorColumn(title = "更新日期")
    private Date updateDate;

    public TradeAccount() {
    }

    public TradeAccount(long id, short siteId, BigDecimal amount) {
        this.id = id;
        this.siteId = siteId;
        this.amount = amount;
    }

    public TradeAccount(long id, short siteId, BigDecimal amount, Date updateDate) {
        this.id = id;
        this.siteId = siteId;
        this.amount = amount;
        this.updateDate = updateDate;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "site_id", nullable = false)
    public short getSiteId() {
        return this.siteId;
    }

    public void setSiteId(short siteId) {
        this.siteId = siteId;
    }

    @Column(name = "amount", nullable = false, precision = 10)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", length = 19)
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
