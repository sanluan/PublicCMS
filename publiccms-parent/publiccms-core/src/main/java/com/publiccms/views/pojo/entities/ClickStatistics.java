package com.publiccms.views.pojo.entities;

/**
 *
 * ClickStatistics
 * 
 */
public class ClickStatistics implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private int clicks;
    private int oldClicks;
    private Short siteId;
    private String url;

    /**
     * @param id
     * @param siteId
     * @param clicks
     * @param oldClicks
     * @param url
     */
    public ClickStatistics(long id, Short siteId, int clicks, int oldClicks, String url) {
        this.id = id;
        this.siteId = siteId;
        this.clicks = clicks;
        this.oldClicks = oldClicks;
        this.url = url;
    }

    /**
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * @return
     */
    public int getClicks() {
        return clicks;
    }

    /**
     */
    public void addClicks() {
        this.clicks++;
    }

    /**
     * @return the oldClicks
     */
    public int getOldClicks() {
        return oldClicks;
    }

    /**
     * @return the siteId
     */
    public Short getSiteId() {
        return siteId;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
}