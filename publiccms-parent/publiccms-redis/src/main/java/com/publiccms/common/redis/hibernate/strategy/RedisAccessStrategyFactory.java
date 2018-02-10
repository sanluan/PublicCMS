package com.publiccms.common.redis.hibernate.strategy;

import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

import com.publiccms.common.redis.hibernate.regions.RedisCollectionRegion;
import com.publiccms.common.redis.hibernate.regions.RedisEntityRegion;
import com.publiccms.common.redis.hibernate.regions.RedisNaturalIdRegion;

/**
 *
 * RedisAccessStrategyFactory
 * 
 */
public interface RedisAccessStrategyFactory {

    /**
     * @param entityRegion
     * @param accessType
     * @return
     */
    public EntityRegionAccessStrategy createEntityRegionAccessStrategy(RedisEntityRegion entityRegion, AccessType accessType);

    /**
     * @param collectionRegion
     * @param accessType
     * @return
     */
    public CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(RedisCollectionRegion collectionRegion,
            AccessType accessType);

    /**
     * @param naturalIdRegion
     * @param accessType
     * @return
     */
    public NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(RedisNaturalIdRegion naturalIdRegion,
            AccessType accessType);

}
