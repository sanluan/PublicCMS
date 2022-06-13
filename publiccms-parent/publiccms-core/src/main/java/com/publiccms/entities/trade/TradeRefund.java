package com.publiccms.entities.trade;
// Generated 2019-6-15 20:00:13 by Hibernate Tools 6.0.0-SNAPSHOT

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.database.CmsUpgrader;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * TradeRefund generated by hbm2java
 */
@Entity
@Table(name = "trade_refund")
public class TradeRefund implements java.io.Serializable {

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
     * user id<p>
     * 用户id
     */
    @GeneratorColumn(title = "用户", condition = true)
    private long userId;
    /**
     * payment id<p>
     * 付款订单id
     */
    @GeneratorColumn(title = "订单", condition = true)
    private long paymentId;
    /**
     * apply amount<p>
     * 申请退款金额
     */
    @GeneratorColumn(title = "申请退款金额")
    private BigDecimal amount;
    /**
     * reason<p>
     * 原因
     */
    @GeneratorColumn(title = "原因")
    private String reason;
    /**
     * update date<p>
     * 更新日期
     */
    @GeneratorColumn(title = "更新日期")
    private Date updateDate;
    /**
     * refund user id<p>
     * 退款操作用户id
     */
    @GeneratorColumn(title = "退款操作用户", condition = true)
    private Long refundUserId;
    /**
     * refund amount<p>
     * 退款金额
     */
    @GeneratorColumn(title = "退款金额")
    private BigDecimal refundAmount;
    /**
     * status(0:pending,1:refunded,2:cancelled,3:refuse,4:fail)<p>
     * 退款状态(0:待处理,1:已退款,2:取消,3:拒绝,4:失败)
     */
    @GeneratorColumn(title = "状态", condition = true)
    private int status;
    /**
     * reply<p>
     * 回复
     */
    @GeneratorColumn(title = "回复")
    private String reply;
    /**
     * create date<p>
     * 创建日期
     */
    @GeneratorColumn(title = "创建日期", order = true)
    private Date createDate;
    /**
     * process date<p>
     * 处理日期
     */
    @GeneratorColumn(title = "处理日期", order = true)
    private Date processingDate;

    public TradeRefund() {
    }

    public TradeRefund(short siteId, long userId, long paymentId, BigDecimal amount, int status, Date createDate) {
        this.siteId = siteId;
        this.userId = userId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status;
        this.createDate = createDate;
    }

    public TradeRefund(short siteId,long userId, long paymentId, BigDecimal amount, String reason, Date updateDate, Long refundUserId,
            BigDecimal refundAmount, int status, String reply, Date createDate, Date processingDate) {
        this.siteId = siteId;
        this.userId = userId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.reason = reason;
        this.updateDate = updateDate;
        this.refundUserId = refundUserId;
        this.refundAmount = refundAmount;
        this.status = status;
        this.reply = reply;
        this.createDate = createDate;
        this.processingDate = processingDate;
    }

    @Id
    @GeneratedValue(generator = "cmsGenerator")
    @GenericGenerator(name = "cmsGenerator", strategy = CmsUpgrader.IDENTIFIER_GENERATOR)
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

    @Column(name = "payment_id", nullable = false)
    public long getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    @Column(name = "amount", nullable = false, precision = 10)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "reason")
    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", length = 19)
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "refund_user_id")
    public Long getRefundUserId() {
        return this.refundUserId;
    }

    public void setRefundUserId(Long refundUserId) {
        this.refundUserId = refundUserId;
    }

    @Column(name = "refund_amount", precision = 10)
    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    @Column(name = "status", nullable = false)
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "reply")
    public String getReply() {
        return this.reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
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
    @Column(name = "processing_date", length = 19)
    public Date getProcessingDate() {
        return this.processingDate;
    }

    public void setProcessingDate(Date processingDate) {
        this.processingDate = processingDate;
    }
}
