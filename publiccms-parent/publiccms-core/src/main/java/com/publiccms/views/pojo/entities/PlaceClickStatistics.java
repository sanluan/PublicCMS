package com.publiccms.views.pojo.entities;

/**
 *
 * ClickStatistics
 * 
 */
public class PlaceClickStatistics extends ClickStatistics {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int maxClicks;

    /**
     * @param id
     * @param siteId
     * @param clicks
     * @param oldClicks
     * @param url
     * @param maxClicks 
     */
    public PlaceClickStatistics(long id, Short siteId, int clicks, int oldClicks, String url,int maxClicks) {
        super(id,siteId,clicks,oldClicks,url);
        this.maxClicks = maxClicks;
        
    }

    /**
     * @return the maxClicks
     */
    public int getMaxClicks() {
        return maxClicks;
    }
}