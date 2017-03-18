package com.sanluan.common.hibernate.redis.strategy;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;

import com.sanluan.common.hibernate.redis.regions.RedisCollectionRegion;

/**
 *
 * NonStrictReadWriteRedisCollectionRegionAccessStrategy
 * 
 */
public class NonStrictReadWriteRedisCollectionRegionAccessStrategy extends AbstractRedisAccessStrategy<RedisCollectionRegion>
        implements CollectionRegionAccessStrategy {

    /**
     * @param region
     * @param options
     */
    public NonStrictReadWriteRedisCollectionRegionAccessStrategy(RedisCollectionRegion region, SessionFactoryOptions options) {
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.hibernate.redis.strategy.AbstractRedisAccessStrategy#
     * putFromLoad(org.hibernate.engine.spi.SessionImplementor,
     * java.lang.Object, java.lang.Object, long, java.lang.Object, boolean)
     */
    @Override
    public boolean putFromLoad(SessionImplementor session, Object key, Object value, long txTimestamp, Object version,
            boolean minimalPutOverride) {
        if (minimalPutOverride && region.contains(key)) {
            return false;
        }
        region.put(key, value);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.hibernate.redis.strategy.AbstractRedisAccessStrategy#
     * get(org.hibernate.engine.spi.SessionImplementor, java.lang.Object, long)
     */
    @Override
    public Object get(SessionImplementor session, Object key, long txTimestamp) throws CacheException {
        return region.get(key);
    }

}
