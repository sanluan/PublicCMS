package com.sanluan.common.hibernate.redis.regions;

import java.util.Properties;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

import com.sanluan.common.cache.redis.RedisClient;
import com.sanluan.common.hibernate.redis.ConfigurableRedisRegionFactory;
import com.sanluan.common.hibernate.redis.strategy.RedisAccessStrategyFactory;

/**
 *
 * RedisNaturalIdRegion
 * 
 */
public class RedisNaturalIdRegion extends RedisTransactionalDataRegion implements NaturalIdRegion {

    /**
     * @param accessStrategyFactory
     * @param redis
     * @param configurableRedisRegionFactory
     * @param regionName
     * @param options
     * @param metadata
     * @param props
     */
    public RedisNaturalIdRegion(RedisAccessStrategyFactory accessStrategyFactory, RedisClient redis,
            ConfigurableRedisRegionFactory configurableRedisRegionFactory, String regionName, SessionFactoryOptions options,
            CacheDataDescription metadata, Properties props) {
        super(accessStrategyFactory, redis, configurableRedisRegionFactory, regionName, options, metadata, props);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.hibernate.cache.spi.NaturalIdRegion#buildAccessStrategy(org.hibernate
     * .cache.spi.access.AccessType)
     */
    @Override
    public NaturalIdRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return accessStrategyFactory.createNaturalIdRegionAccessStrategy(this, accessType);
    }
}
