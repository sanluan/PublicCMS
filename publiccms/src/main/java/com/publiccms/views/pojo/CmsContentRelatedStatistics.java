package com.publiccms.views.pojo;

import com.publiccms.entities.cms.CmsContentRelated;

public class CmsContentRelatedStatistics implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private int clicks;
    private CmsContentRelated entity;

    public CmsContentRelatedStatistics(long id, int clicks, CmsContentRelated entity) {
        this.clicks = clicks;
        this.id = id;
        this.entity = entity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public CmsContentRelated getEntity() {
        return entity;
    }

    public void setEntity(CmsContentRelated entity) {
        this.entity = entity;
    }
}