package com.publiccms.entities.trade;
// Generated 2023-8-7 21:42:23 by Hibernate Tools 5.6.15.Final

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.generator.annotation.GeneratorColumn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * TradeAddress generated by hbm2java
 */
@Entity
@Table(name = "trade_address")
public class TradeAddress implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @GeneratorColumn(title = "ID")
    private Long id;
    @GeneratorColumn(title = "站点", condition = true)
    @JsonIgnore
    private short siteId;
    /**
     * user

     * 用户
     */
    @GeneratorColumn(title = "用户", condition = true)
    private long userId;
    /**
     * address

     * 地址
     */
    @GeneratorColumn(title = "地址")
    private String address;
    /**
     * addressee

     * 收件人
     */
    @GeneratorColumn(title = "收件人")
    private String addressee;
    /**
     * telephone

     * 电话
     */
    @GeneratorColumn(title = "电话")
    private String telephone;
    /**
     * create date

     * 创建日期
     */
    @GeneratorColumn(title = "createDate")
    private Date createDate;

    public TradeAddress() {
    }

    public TradeAddress(short siteId, long userId, Date createDate) {
        this.siteId = siteId;
        this.userId = userId;
        this.createDate = createDate;
    }

    public TradeAddress(short siteId, long userId, String address, String addressee, String telephone, Date createDate) {
        this.siteId = siteId;
        this.userId = userId;
        this.address = address;
        this.addressee = addressee;
        this.telephone = telephone;
        this.createDate = createDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "site_id", nullable = false)
    public short getSiteId() {
        return this.siteId;
    }

    public void setSiteId(short siteId) {
        this.siteId = siteId;
    }

    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "address")
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "addressee", length = 50)
    public String getAddressee() {
        return this.addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    @Column(name = "telephone", length = 50)
    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
