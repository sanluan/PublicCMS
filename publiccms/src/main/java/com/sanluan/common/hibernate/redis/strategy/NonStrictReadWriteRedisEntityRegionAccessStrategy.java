
package com.sanluan.common.hibernate.redis.strategy;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;

import com.sanluan.common.hibernate.redis.regions.RedisEntityRegion;

/**
 *
 * NonStrictReadWriteRedisEntityRegionAccessStrategy
 * 
 */
public class NonStrictReadWriteRedisEntityRegionAccessStrategy extends AbstractRedisAccessStrategy<RedisEntityRegion>
        implements EntityRegionAccessStrategy {

    /**
     * @param region
     * @param options
     */
    public NonStrictReadWriteRedisEntityRegionAccessStrategy(RedisEntityRegion region, SessionFactoryOptions options) {
        super(region, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#
     * generateCacheKey(java.lang.Object,
     * org.hibernate.persister.entity.EntityPersister,
     * org.hibernate.engine.spi.SessionFactoryImplementor, java.lang.String)
     */
    @Override
    public Object generateCacheKey(Object id, EntityPersister persister, SessionFactoryImplementor factory,
            String tenantIdentifier) {
        return DefaultCacheKeysFactory.staticCreateEntityKey(id, persister, factory, tenantIdentifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.hibernate.cache.spi.access.EntityRegionAccessStrategy#getCacheKeyId(
     * java.lang.Object)
     */
    @Override
    public Object getCacheKeyId(Object cacheKey) {
        return DefaultCacheKeysFactory.staticGetEntityId(cacheKey);
    }

    @Override
    public EntityRegion getRegion() {
        return region;
    }

    @Override
    public Object get(SessionImplementor session, Object key, long txTimestamp) {
        return region.get(key);
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

    @Override
    public SoftLock lockItem(SessionImplementor session, Object key, Object version) {
        return null;
    }

    @Override
    public void unlockItem(SessionImplementor session, Object key, SoftLock lock) {
        region.remove(key);
    }

    @Override
    public boolean insert(SessionImplementor session, Object key, Object value, Object version) {
        return false;
    }

    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value, Object version) {
        return false;
    }

    @Override
    public boolean update(SessionImplementor session, Object key, Object value, Object currentVersion, Object previousVersion) {
        remove(session, key);
        return false;
    }

    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, Object currentVersion,
            Object previousVersion, SoftLock lock) {
        unlockItem(session, key, lock);
        return false;
    }
}
