package com.sanluan.common.hibernate.redis.strategy;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;

import com.sanluan.common.hibernate.redis.regions.RedisEntityRegion;

/**
 *
 * ReadOnlyRedisEntityRegionAccessStrategy
 * 
 */
public class ReadOnlyRedisEntityRegionAccessStrategy extends AbstractRedisAccessStrategy<RedisEntityRegion>
        implements EntityRegionAccessStrategy {

    /**
     * @param region
     * @param options
     */
    public ReadOnlyRedisEntityRegionAccessStrategy(RedisEntityRegion region, SessionFactoryOptions options) {
        super(region, options);
    }

    @Override
    public Object generateCacheKey(Object id, EntityPersister persister, SessionFactoryImplementor factory,
            String tenantIdentifier) {
        return null;
    }

    @Override
    public Object getCacheKeyId(Object cacheKey) {
        return null;
    }

    @Override
    public EntityRegion getRegion() {
        return region;
    }

    /* (non-Javadoc)
     * @see com.sanluan.common.hibernate.redis.strategy.AbstractRedisAccessStrategy#get(org.hibernate.engine.spi.SessionImplementor, java.lang.Object, long)
     */
    @Override
    public Object get(SessionImplementor session, Object key, long txTimestamp) {
        return region.get(key);
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
        evict(key);
    }

    @Override
    public boolean insert(SessionImplementor session, Object key, Object value, Object version) {
        return false;
    }

    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value, Object version) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean update(SessionImplementor session, Object key, Object value, Object currentVersion, Object previousVersion) {
        throw new UnsupportedOperationException("Can't write to a readonly object");
    }

    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, Object currentVersion,
            Object previousVersion, SoftLock lock) {
        throw new UnsupportedOperationException("Can't write to a readonly object");
    }
}
