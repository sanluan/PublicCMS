package com.sanluan.common.hibernate.redis.strategy;

import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

import com.sanluan.common.hibernate.redis.regions.RedisCollectionRegion;
import com.sanluan.common.hibernate.redis.regions.RedisEntityRegion;
import com.sanluan.common.hibernate.redis.regions.RedisNaturalIdRegion;

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
