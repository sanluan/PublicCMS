package com.publiccms.views.pojo;

import com.publiccms.entities.cms.CmsContentRelated;
import com.sanluan.common.base.Base;

public class CmsContentRelatedStatistics extends Base {
    private int id;
    private int clicks;
    private CmsContentRelated entity;

    public CmsContentRelatedStatistics(int id, int clicks, CmsContentRelated entity) {
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

    public CmsContentRelated getEntity() {
        return entity;
    }

    public void setEntity(CmsContentRelated entity) {
        this.entity = entity;
    }
}