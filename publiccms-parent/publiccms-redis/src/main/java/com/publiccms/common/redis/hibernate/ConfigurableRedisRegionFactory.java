package com.publiccms.common.redis.hibernate;

import com.publiccms.common.redis.DatabaseRedisClient;
import com.publiccms.common.redis.hibernate.timestamper.CacheTimestamper;

/**
 *
 * ConfigurableRedisRegionFactory
 * 
 */
public interface ConfigurableRedisRegionFactory {
    
    /**
     * @param redisClient
     * @param cacheKey
     * @return
     */
    public CacheTimestamper createCacheTimestamper(DatabaseRedisClient redisClient, String cacheKey);
}
