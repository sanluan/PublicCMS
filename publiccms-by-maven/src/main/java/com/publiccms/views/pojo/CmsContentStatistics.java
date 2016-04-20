package com.publiccms.views.pojo;

import com.publiccms.entities.cms.CmsContent;
import com.sanluan.common.base.Base;

public class CmsContentStatistics extends Base {
    private int id;
    private int clicks;
    private int comments;
    private int scores;
    private CmsContent entity;

    public CmsContentStatistics(int id, int clicks, int comments, int scores, CmsContent entity) {
        this.clicks = clicks;
        this.comments = comments;
        this.scores = scores;
        this.id = id;
        this.entity = entity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public CmsContent getEntity() {
        return entity;
    }

    public void setEntity(CmsContent entity) {
        this.entity = entity;
    }
}