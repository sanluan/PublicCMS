package com.publiccms.views.pojo;

import com.publiccms.entities.cms.CmsPlace;

public class CmsPlaceStatistics implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private int clicks;
    private CmsPlace entity;

    public CmsPlaceStatistics(long id, int clicks, CmsPlace entity) {
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

    public CmsPlace getEntity() {
        return entity;
    }

    public void setEntity(CmsPlace entity) {
        this.entity = entity;
    }
}