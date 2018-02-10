package com.publiccms.common.api;

/**
 *
 * SiteCache
 * 
 */
public interface SiteCache extends Cache {
    
    /**
     * @param siteId
     */
    public void clear(short siteId);
}
