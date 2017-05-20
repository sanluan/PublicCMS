package org.publiccms.views.pojo;

import org.publiccms.entities.cms.CmsPlace;

/**
 *
 * CmsPlaceStatistics
 * 
 */
public class CmsPlaceStatistics implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private int clicks;
    private CmsPlace entity;

    /**
     * @param id
     * @param clicks
     * @param entity
     */
    public CmsPlaceStatistics(long id, int clicks, CmsPlace entity) {
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
    public CmsPlace getEntity() {
        return entity;
    }

    /**
     * @param entity
     */
    public void setEntity(CmsPlace entity) {
        this.entity = entity;
    }
}