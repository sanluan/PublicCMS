package org.publiccms.common.api;

/**
 *
 * SiteCache
 * 
 */
public interface SiteCache extends Cache {
    
    /**
     * @param siteId
     */
    public void clear(int siteId);
}
