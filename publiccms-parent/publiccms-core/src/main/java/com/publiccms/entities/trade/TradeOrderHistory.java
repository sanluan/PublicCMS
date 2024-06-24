package com.publiccms.entities.trade;
// Generated 2019-6-16 9:09:11 by Hibernate Tools 6.0.0-SNAPSHOT

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.database.IDStyleGenerator;
import com.publiccms.common.generator.annotation.GeneratorColumn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * TradeProductOrderHistory generated by hbm2java
 */
@Entity
@Table(name = "trade_order_history")
public class TradeOrderHistory implements java.io.Serializable {
    /**
    *
    */
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
     * order id<p>
     * 订单id
     */
    @GeneratorColumn(title = "订单", condition = true)
    private long orderId;
    /**
     * create date<p>
     * 创建日期
     */
    @GeneratorColumn(title = "创建日期", condition = true, order = true)
    private Date createDate;
    /**
     * operate<p>
     * 操作
     */
    @GeneratorColumn(title = "操作", condition = true)
    private String operate;
    /**
     * content<p>
     * 内容
     */
    @GeneratorColumn(title = "内容", condition = true, like = true)
    private String content;

    public TradeOrderHistory() {
    }

    public TradeOrderHistory(short siteId, long orderId, Date createDate, String operate) {
        this.siteId = siteId;
        this.orderId = orderId;
        this.createDate = createDate;
        this.operate = operate;
    }

    public TradeOrderHistory(short siteId, long orderId, Date createDate, String operate, String content) {
        this.siteId = siteId;
        this.orderId = orderId;
        this.createDate = createDate;
        this.operate = operate;
        this.content = content;
    }

    @Id
    @GeneratedValue(generator = "cmsGenerator")
    @GenericGenerator(name = "cmsGenerator", type = IDStyleGenerator.class)

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

    @Column(name = "order_id", nullable = false)
    public long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, length = 19)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name="operate", nullable=false, length=100)
    public String getOperate() {
        return this.operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    @Column(name = "content", length = 65535)
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
