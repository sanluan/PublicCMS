package com.sanluan.common.hibernate.redis.strategy;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;

import com.sanluan.common.hibernate.redis.regions.RedisNaturalIdRegion;

/**
 *
 * ReadWriteRedisNaturalIdRegionAccessStrategy
 * 
 */
public class ReadWriteRedisNaturalIdRegionAccessStrategy extends AbstractReadWriteRedisAccessStrategy<RedisNaturalIdRegion>
        implements NaturalIdRegionAccessStrategy {

    /**
     * @param region
     * @param options
     */
    public ReadWriteRedisNaturalIdRegionAccessStrategy(RedisNaturalIdRegion region, SessionFactoryOptions options) {
        super(region, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy#
     * generateCacheKey(java.lang.Object[],
     * org.hibernate.persister.entity.EntityPersister,
     * org.hibernate.engine.spi.SessionImplementor)
     */
    @Override
    public Object generateCacheKey(Object[] naturalIdValues, EntityPersister persister, SessionImplementor session) {
        return DefaultCacheKeysFactory.staticCreateNaturalIdKey(naturalIdValues, persister, session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy#
     * getNaturalIdValues(java.lang.Object)
     */
    @Override
    public Object[] getNaturalIdValues(Object cacheKey) {
        return DefaultCacheKeysFactory.staticGetNaturalIdValues(cacheKey);
    }

    @Override
    public NaturalIdRegion getRegion() {
        return region;
    }

    @Override
    public boolean insert(SessionImplementor session, Object key, Object value) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean update(SessionImplementor session, Object key, Object value) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, SoftLock lock) {
        region.put(key, value);
        return true;
    }
}
