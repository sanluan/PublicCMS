package com.publiccms.views.pojo.entities;

/**
 *
 * CmsContentStatistics
 * 
 */
public class CmsContentStatistics extends ClickStatistics implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int scores;

    /**
     * @param id
     * @param siteId
     * @param clicks
     * @param oldClicks
     * @param scores
     * @param url
     */
    public CmsContentStatistics(long id, Short siteId, int clicks, int scores, int oldClicks, String url) {
        super(id, siteId, clicks, oldClicks, url);
        this.scores = scores;
    }

    /**
     * @return
     */
    public int getScores() {
        return scores;
    }

    /**
     * @param add 
     */
    public void addScores(boolean add) {
        if (add) {
            this.scores++;
        } else {
            this.scores--;
        }
    }
}