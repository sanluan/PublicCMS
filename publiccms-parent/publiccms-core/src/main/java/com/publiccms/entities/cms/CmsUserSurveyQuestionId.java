package com.publiccms.entities.cms;
// Generated 2021-09-23 16:55:08 by Hibernate Tools 6.0.0-SNAPSHOT

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * CmsUserSurveyQuestionId generated by hbm2java
 */
@Embeddable
public class CmsUserSurveyQuestionId implements java.io.Serializable {

    /**
    *
    */
    private static final long serialVersionUID = 1L;
    /**
     * user id<p>
     * 用户id
     */
    @GeneratorColumn(title = "用户", condition = true)
    private long userId;
    /**
     * question id<p>
     * 问题id
     */
    @GeneratorColumn(title = "问题ID", condition = true)
    private long questionId;

    public CmsUserSurveyQuestionId() {
    }

    public CmsUserSurveyQuestionId(long userId, long questionId) {
        this.userId = userId;
        this.questionId = questionId;
    }

    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "question_id", nullable = false)
    public long getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof CmsUserSurveyQuestionId))
            return false;
        CmsUserSurveyQuestionId castOther = (CmsUserSurveyQuestionId) other;
        return (this.getUserId() == castOther.getUserId()) && (this.getQuestionId() == castOther.getQuestionId());
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (int) this.getUserId();
        result = 37 * result + (int) this.getQuestionId();
        return result;
    }

}
