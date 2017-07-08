package org.publiccms.views.pojo;

import org.publiccms.entities.cms.CmsContent;

/**
 *
 * CmsContentStatistics
 * 
 */
public class CmsContentStatistics implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
    private int clicks;
    private int comments;
    private int scores;
    private CmsContent entity;

    /**
     * @param id
     * @param clicks
     * @param comments
     * @param scores
     * @param entity
     */
    public CmsContentStatistics(long id, int clicks, int comments, int scores, CmsContent entity) {
        this.clicks = clicks;
        this.comments = comments;
        this.scores = scores;
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
    public int getComments() {
        return comments;
    }

    /**
     * @param comments
     */
    public void setComments(int comments) {
        this.comments = comments;
    }

    /**
     * @return
     */
    public int getScores() {
        return scores;
    }

    /**
     * @param scores
     */
    public void setScores(int scores) {
        this.scores = scores;
    }

    /**
     * @return
     */
    public CmsContent getEntity() {
        return entity;
    }

    /**
     * @param entity
     */
    public void setEntity(CmsContent entity) {
        this.entity = entity;
    }
}