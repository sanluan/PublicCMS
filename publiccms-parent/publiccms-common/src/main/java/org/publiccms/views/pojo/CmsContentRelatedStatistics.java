package org.publiccms.views.pojo;

import org.publiccms.entities.cms.CmsContentRelated;

/**
 *
 * CmsContentRelatedStatistics
 * 
 */
public class CmsContentRelatedStatistics implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private int clicks;
    private CmsContentRelated entity;

    /**
     * @param id
     * @param clicks
     * @param entity
     */
    public CmsContentRelatedStatistics(long id, int clicks, CmsContentRelated entity) {
        this.clicks = clicks;
        this.id = id;
        this.entity = entity;
    }

    /**
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return
     */
    public int getClicks() {
        return clicks;
    }

    /**
     * @param clicks
     */
    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    /**
     * @return
     */
    public CmsContentRelated getEntity() {
        return entity;
    }

    /**
     * @param entity
     */
    public void setEntity(CmsContentRelated entity) {
        this.entity = entity;
    }
}