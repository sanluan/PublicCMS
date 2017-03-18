package com.publiccms.views.pojo;

import com.publiccms.entities.cms.CmsTag;

public class CmsTagStatistics implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private int searchCounts;
    private CmsTag entity;

    public CmsTagStatistics(long id, int searchCounts, CmsTag entity) {
        this.searchCounts = searchCounts;
        this.id = id;
        this.entity = entity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSearchCounts() {
        return searchCounts;
    }

    public void setSearchCounts(int searchCounts) {
        this.searchCounts = searchCounts;
    }

    public CmsTag getEntity() {
        return entity;
    }

    public void setEntity(CmsTag entity) {
        this.entity = entity;
    }
}