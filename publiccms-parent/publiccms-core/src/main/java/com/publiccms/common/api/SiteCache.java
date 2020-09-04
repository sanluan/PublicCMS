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
    void clear(short siteId);
}
