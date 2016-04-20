package com.publiccms.views.pojo;

import com.publiccms.entities.cms.CmsPlace;
import com.sanluan.common.base.Base;

public class CmsPlaceStatistics extends Base {
    private int id;
    private int clicks;
    private CmsPlace entity;

    public CmsPlaceStatistics(int id, int clicks, CmsPlace entity) {
        this.clicks = clicks;
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

    public CmsPlace getEntity() {
        return entity;
    }

    public void setEntity(CmsPlace entity) {
        this.entity = entity;
    }
}