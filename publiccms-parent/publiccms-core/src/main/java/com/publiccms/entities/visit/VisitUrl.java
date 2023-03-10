package com.publiccms.entities.visit;
// Generated 2021-8-6 14:20:45 by Hibernate Tools 5.4.32.Final

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * VisitUrl generated by hbm2java
 */
@Entity
@Table(name = "visit_url")
@DynamicUpdate
public class VisitUrl implements java.io.Serializable {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @GeneratorColumn(title = "ID")
    private VisitUrlId id;
    /**
     * url<p>
     * 网址
     */
    @GeneratorColumn(title = "URL")
    @Length(max = 2048)
    private String url;
    /**
     * pv
     */
    @GeneratorColumn(title = "PV")
    private long pv;
    /**
     * uv
     */
    @GeneratorColumn(title = "UV")
    private Long uv;
    /**
     * ip views<p>
     * ip访问数
     */
    @GeneratorColumn(title = "IP Views")
    private Long ipviews;

    public VisitUrl() {
    }

    public VisitUrl(VisitUrlId id, String url, long pv) {
        this.id = id;
        this.url = url;
        this.pv = pv;
    }

    public VisitUrl(short siteId, Date visitDate, String url, long pv, Long uv, Long ipviews) {
        this.id = new VisitUrlId(siteId, visitDate, null, null);
        this.url = url;
        this.pv = pv;
        this.uv = uv;
        this.ipviews = ipviews;
    }

    public VisitUrl(VisitUrlId id, String url, long pv, Long uv, Long ipviews) {
        this.id = id;
        this.url = url;
        this.pv = pv;
        this.uv = uv;
        this.ipviews = ipviews;
    }

    @EmbeddedId

    @AttributeOverrides({ @AttributeOverride(name = "siteId", column = @Column(name = "site_id", nullable = false)),
            @AttributeOverride(name = "visitDate", column = @Column(name = "visit_date", nullable = false, length = 10)),
            @AttributeOverride(name = "urlMd5", column = @Column(name = "url_md5", nullable = false, length = 50)),
            @AttributeOverride(name = "urlSha", column = @Column(name = "url_sha", nullable = false, length = 100)) })
    public VisitUrlId getId() {
        return this.id;
    }

    public void setId(VisitUrlId id) {
        this.id = id;
    }

    @Column(name = "url", nullable = false, length = 2048)
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "pv", nullable = false)
    public long getPv() {
        return this.pv;
    }

    public void setPv(long pv) {
        this.pv = pv;
    }

    @Column(name = "uv")
    public Long getUv() {
        return this.uv;
    }

    public void setUv(Long uv) {
        this.uv = uv;
    }

    @Column(name = "ipviews")
    public Long getIpviews() {
        return this.ipviews;
    }

    public void setIpviews(Long ipviews) {
        this.ipviews = ipviews;
    }

}
