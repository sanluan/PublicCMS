package com.sanluan.common.hibernate.redis.regions;

import java.util.Properties;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;

import com.sanluan.common.cache.redis.RedisClient;
import com.sanluan.common.hibernate.redis.ConfigurableRedisRegionFactory;
import com.sanluan.common.hibernate.redis.strategy.RedisAccessStrategyFactory;

/**
 *
 * RedisCollectionRegion
 * 
 */
public class RedisCollectionRegion extends RedisTransactionalDataRegion implements CollectionRegion {

    /**
     * @param accessStrategyFactory
     * @param redis
     * @param configurableRedisRegionFactory
     * @param regionName
     * @param options
     * @param metadata
     * @param props
     */
    public RedisCollectionRegion(RedisAccessStrategyFactory accessStrategyFactory, RedisClient redis,
            ConfigurableRedisRegionFactory configurableRedisRegionFactory, String regionName, SessionFactoryOptions options,
            CacheDataDescription metadata, Properties props) {
        super(accessStrategyFactory, redis, configurableRedisRegionFactory, regionName, options, metadata, props);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.cache.spi.CollectionRegion#buildAccessStrategy(org.
     * hibernate.cache.spi.access.AccessType)
     */
    @Override
    public CollectionRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return accessStrategyFactory.createCollectionRegionAccessStrategy(this, accessType);
    }
}
