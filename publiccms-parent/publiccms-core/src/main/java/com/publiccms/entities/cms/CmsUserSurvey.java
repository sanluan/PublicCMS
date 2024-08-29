package com.publiccms.entities.cms;
// Generated 2021-09-23 16:55:08 by Hibernate Tools 6.0.0-SNAPSHOT

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * CmsUserSurvey generated by hbm2java
 */
@Entity
@Table(name = "cms_user_survey")
@DynamicUpdate
public class CmsUserSurvey implements java.io.Serializable {

    /**
    *
    */
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @GeneratorColumn(title = "ID")
    private CmsUserSurveyId id;
    @JsonIgnore
    @GeneratorColumn(title = "站点", condition = true)
    private short siteId;
    /**
     * anonymous

     * 匿名用户
     */
    @GeneratorColumn(title = "匿名用户", condition = true)
    private boolean anonymous;
    /**
     * score

     * 分数
     */
    @GeneratorColumn(title = "分数", order = true)
    private Integer score;
    /**
     * ip
     */
    @GeneratorColumn(title = "IP", condition = true, like = true)
    private String ip;
    /**
     * create date

     * 创建日期
     */
    @GeneratorColumn(title = "创建日期", order = true)
    private Date createDate;

    public CmsUserSurvey() {
    }

    public CmsUserSurvey(short siteId, boolean anonymous, String ip, Date createDate) {
        this.siteId = siteId;
        this.anonymous = anonymous;
        this.ip = ip;
        this.createDate = createDate;
    }

    public CmsUserSurvey(short siteId, boolean anonymous, Integer score, String ip, Date createDate) {
        this.siteId = siteId;
        this.anonymous = anonymous;
        this.score = score;
        this.ip = ip;
        this.createDate = createDate;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)),
            @AttributeOverride(name = "questionId", column = @Column(name = "question_id", nullable = false)) })
    public CmsUserSurveyId getId() {
        return this.id;
    }

    public void setId(CmsUserSurveyId id) {
        this.id = id;
    }

    @Column(name = "site_id", nullable = false)
    public short getSiteId() {
        return this.siteId;
    }

    public void setSiteId(short siteId) {
        this.siteId = siteId;
    }

    @Column(name = "anonymous", nullable = false)
    public boolean isAnonymous() {
        return this.anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    @Column(name = "score")
    public Integer getScore() {
        return this.score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Column(name = "ip", nullable = false, length = 130)
    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
