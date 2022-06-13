package com.publiccms.entities.visit;
// Generated 2021-1-14 22:33:12 by Hibernate Tools 6.0.0-SNAPSHOT

import java.util.Date;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * VisitSession generated by hbm2java
 */
@Entity
@Table(name = "visit_session")
@DynamicUpdate
public class VisitSession implements java.io.Serializable {

    /**
    *
    */
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @GeneratorColumn(title = "ID")
    private VisitSessionId id;
    /**
     * last visit date<p>
     * 上次访问日期
     */
    @GeneratorColumn(title = "上次访问时间", order = true)
    private Date lastVisitDate;
    /**
     * first visit date<p>
     * 首次访问日期
     */
    @GeneratorColumn(title = "首次访问时间")
    private Date firstVisitDate;
    /**
     * ip
     */
    @GeneratorColumn(title = "IP", condition = true)
    private String ip;
    /**
     * pv
     */
    @GeneratorColumn(title = "PV")
    private long pv;

    public VisitSession() {
    }

    public VisitSession(VisitSessionId id, String ip, long pv) {
        this.id = id;
        this.ip = ip;
        this.pv = pv;
    }

    public VisitSession(short siteId, String sessionId, Date visitDate, Date lastVisitDate, Date firstVisitDate, String ip,
            long pv) {
        this.id = new VisitSessionId(siteId, sessionId, visitDate);
        this.lastVisitDate = lastVisitDate;
        this.firstVisitDate = firstVisitDate;
        this.ip = ip;
        this.pv = pv;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "siteId", column = @Column(name = "site_id", nullable = false)),
            @AttributeOverride(name = "sessionId", column = @Column(name = "session_id", nullable = false, length = 50)),
            @AttributeOverride(name = "visitDate", column = @Column(name = "visit_date", nullable = false, length = 10)) })
    public VisitSessionId getId() {
        return this.id;
    }

    public void setId(VisitSessionId id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_visit_date", length = 19)
    public Date getLastVisitDate() {
        return this.lastVisitDate;
    }

    public void setLastVisitDate(Date lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "first_visit_date", length = 19)
    public Date getFirstVisitDate() {
        return this.firstVisitDate;
    }

    public void setFirstVisitDate(Date firstVisitDate) {
        this.firstVisitDate = firstVisitDate;
    }

    @Column(name = "ip", nullable = false, length = 130)
    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "pv", nullable = false)
    public long getPv() {
        return this.pv;
    }

    public void setPv(long pv) {
        this.pv = pv;
    }

}
