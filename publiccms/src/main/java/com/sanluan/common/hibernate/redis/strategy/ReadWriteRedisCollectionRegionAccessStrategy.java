package com.sanluan.common.hibernate.redis.strategy;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.collection.CollectionPersister;

import com.sanluan.common.hibernate.redis.regions.RedisCollectionRegion;

/**
 *
 * ReadWriteRedisCollectionRegionAccessStrategy
 * 
 */
public class ReadWriteRedisCollectionRegionAccessStrategy extends AbstractReadWriteRedisAccessStrategy<RedisCollectionRegion>
        implements CollectionRegionAccessStrategy {

    /**
     * @param region
     * @param options
     */
    public ReadWriteRedisCollectionRegionAccessStrategy(RedisCollectionRegion region, SessionFactoryOptions options) {
        super(region, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.cache.spi.access.CollectionRegionAccessStrategy#
     * generateCacheKey(java.lang.Object,
     * org.hibernate.persister.collection.CollectionPersister,
     * org.hibernate.engine.spi.SessionFactoryImplementor, java.lang.String)
     */
    @Override
    public Object generateCacheKey(Object id, CollectionPersister persister, SessionFactoryImplementor factory,
            String tenantIdentifier) {
        return DefaultCacheKeysFactory.staticCreateCollectionKey(id, persister, factory, tenantIdentifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.cache.spi.access.CollectionRegionAccessStrategy#
     * getCacheKeyId(java.lang.Object)
     */
    @Override
    public Object getCacheKeyId(Object cacheKey) {
        return DefaultCacheKeysFactory.staticGetCollectionId(cacheKey);
    }

    @Override
    public CollectionRegion getRegion() {
        return region;
    }
}
