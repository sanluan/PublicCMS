package com.publiccms.entities.trade;
// Generated 2019-6-15 18:43:34 by Hibernate Tools 6.0.0-SNAPSHOT

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.database.CmsUpgrader;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * TradeOrder generated by hbm2java
 */
@Entity
@Table(name = "trade_payment")
public class TradePayment implements java.io.Serializable {

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
     * amount<p>
     * 金额
     */
    @GeneratorColumn(title = "金额")
    private BigDecimal amount;
    /**
     * description<p>
     * 描述
     */
    @GeneratorColumn(title = "描述")
    @Length(max = 255)
    private String description;
    /**
     * trade type(recharge,product)<p>
     * 订单类型(recharge:充值,product:产品)
     */
    @GeneratorColumn(title = "订单类型", condition = true)
    private String tradeType;
    /**
     * serial number<p>
     * 订单流水
     */
    @GeneratorColumn(title = "订单流水", condition = true)
    private String serialNumber;
    /**
     * account type(account,alipay,wechat)<p>
     * 账户类型(account:账户,alipay:支付宝,wechat:微信)
     */
    @GeneratorColumn(title = "账户类型", condition = true)
    private String accountType;
    /**
     * account serial number<p>
     * 账户流水
     */
    @GeneratorColumn(title = "账户流水", condition = true)
    private String accountSerialNumber;
    /**
     * ip
     */
    @GeneratorColumn(title = "IP")
    private String ip;
    /**
     * status(0:pending pay,1:paid,2:pending refund,3:refunded,4:closed)<p>
     * 状态(0:待支付,1:已支付,2:待退款,3:已退款,4:已关闭)
     */
    @GeneratorColumn(title = "状态", condition = true)
    private int status;
    /**
     * processed<p>
     * 已处理
     */
    @GeneratorColumn(title = "已处理", condition = true)
    private boolean processed;
    /**
     * process user id<p>
     * 处理用户id
     */
    @GeneratorColumn(title = "处理用户")
    private Long processUserId;
    /**
     * update date<p>
     * 更新日期
     */
    @GeneratorColumn(title = "更新日期")
    private Date updateDate;
    /**
     * create date<p>
     * 创建日期
     */
    @GeneratorColumn(title = "创建日期", condition = true, order = true)
    private Date createDate;
    /**
     * process date<p>
     * 处理日期
     */
    @GeneratorColumn(title = "处理日期")
    private Date processDate;
    /**
     * payment date<p>
     * 支付日期
     */
    @GeneratorColumn(title = "支付日期")
    private Date paymentDate;

    public TradePayment() {
    }

    public TradePayment(short siteId, long userId, BigDecimal amount, String tradeType, String serialNumber, String accountType,
            String ip, int status, boolean processed, Date createDate) {
        this.siteId = siteId;
        this.userId = userId;
        this.amount = amount;
        this.tradeType = tradeType;
        this.serialNumber = serialNumber;
        this.accountType = accountType;
        this.ip = ip;
        this.status = status;
        this.processed = processed;
        this.createDate = createDate;
    }

    public TradePayment(short siteId, long userId, BigDecimal amount, String description, String tradeType, String serialNumber,
            String accountType, String accountSerialNumber, String ip, int status, boolean processed, Date updateDate,
            Date createDate, Date processDate, Date paymentDate) {
        this.siteId = siteId;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.tradeType = tradeType;
        this.serialNumber = serialNumber;
        this.accountType = accountType;
        this.accountSerialNumber = accountSerialNumber;
        this.ip = ip;
        this.status = status;
        this.processed = processed;
        this.updateDate = updateDate;
        this.createDate = createDate;
        this.processDate = processDate;
        this.paymentDate = paymentDate;
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

    @Column(name = "amount", nullable = false, precision = 10)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "trade_type", nullable = false, length = 20)
    public String getTradeType() {
        return this.tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    @Column(name = "serial_number", nullable = false, length = 100)
    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column(name = "account_type", nullable = false, length = 20)
    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Column(name = "account_serial_number", length = 100)
    public String getAccountSerialNumber() {
        return this.accountSerialNumber;
    }

    public void setAccountSerialNumber(String accountSerialNumber) {
        this.accountSerialNumber = accountSerialNumber;
    }

    @Column(name = "ip", nullable = false, length = 130)
    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "status", nullable = false)
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "processed", nullable = false)
    public boolean isProcessed() {
        return this.processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Column(name = "process_user_id")
    public Long getProcessUserId() {
        return this.processUserId;
    }

    public void setProcessUserId(Long processUserId) {
        this.processUserId = processUserId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", length = 19)
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
    @Column(name = "process_date", length = 19)
    public Date getProcessDate() {
        return this.processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date", length = 19)
    public Date getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

}
