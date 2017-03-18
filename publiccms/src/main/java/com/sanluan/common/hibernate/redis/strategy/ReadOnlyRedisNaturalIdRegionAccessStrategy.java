package com.sanluan.common.hibernate.redis.strategy;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;

import com.sanluan.common.hibernate.redis.regions.RedisNaturalIdRegion;

public class ReadOnlyRedisNaturalIdRegionAccessStrategy extends AbstractRedisAccessStrategy<RedisNaturalIdRegion>
        implements NaturalIdRegionAccessStrategy {

    /**
     * @param region
     * @param options
     */
    public ReadOnlyRedisNaturalIdRegionAccessStrategy(RedisNaturalIdRegion region, SessionFactoryOptions options) {
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

    @Override
    public Object[] getNaturalIdValues(Object cacheKey) {
        return DefaultCacheKeysFactory.staticGetNaturalIdValues(cacheKey);
    }

    @Override
    public NaturalIdRegion getRegion() {
        return region;
    }

    /* (non-Javadoc)
     * @see com.sanluan.common.hibernate.redis.strategy.AbstractRedisAccessStrategy#putFromLoad(org.hibernate.engine.spi.SessionImplementor, java.lang.Object, java.lang.Object, long, java.lang.Object, boolean)
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

    @Override
    public SoftLock lockItem(SessionImplementor session, Object key, Object version) {
        return null;
    }

    @Override
    public void unlockItem(SessionImplementor session, Object key, SoftLock lock) {
        region.remove(key);
    }

    @Override
    public boolean insert(SessionImplementor session, Object key, Object value) {
        return false;
    }

    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean update(SessionImplementor session, Object key, Object value) {
        throw new UnsupportedOperationException("Can't write to a readonly object");
    }

    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, SoftLock lock) {
        throw new UnsupportedOperationException("Can't write to a readonly object");
    }
}
