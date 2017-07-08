package org.publiccms.views.pojo;

import org.publiccms.entities.cms.CmsTag;

/**
 *
 * CmsTagStatistics
 * 
 */
public class CmsTagStatistics implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private int searchCounts;
    private CmsTag entity;

    /**
     * @param id
     * @param searchCounts
     * @param entity
     */
    public CmsTagStatistics(long id, int searchCounts, CmsTag entity) {
        this.searchCounts = searchCounts;
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
    public int getSearchCounts() {
        return searchCounts;
    }

    /**
     * @param searchCounts
     */
    public void setSearchCounts(int searchCounts) {
        this.searchCounts = searchCounts;
    }

    /**
     * @return
     */
    public CmsTag getEntity() {
        return entity;
    }

    /**
     * @param entity
     */
    public void setEntity(CmsTag entity) {
        this.entity = entity;
    }
}