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
 * ReadWriteRedisEntityRegionAccessStrategy
 * 
 */
public class ReadWriteRedisEntityRegionAccessStrategy extends AbstractReadWriteRedisAccessStrategy<RedisEntityRegion>
        implements EntityRegionAccessStrategy {

    /**
     * @param region
     * @param options
     */
    public ReadWriteRedisEntityRegionAccessStrategy(RedisEntityRegion region, SessionFactoryOptions options) {
        super(region, options);
    }

    /* (non-Javadoc)
     * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#generateCacheKey(java.lang.Object, org.hibernate.persister.entity.EntityPersister, org.hibernate.engine.spi.SessionFactoryImplementor, java.lang.String)
     */
    @Override
    public Object generateCacheKey(Object id, EntityPersister persister, SessionFactoryImplementor factory,
            String tenantIdentifier) {
        return DefaultCacheKeysFactory.staticCreateEntityKey(id, persister, factory, tenantIdentifier);
    }

    /* (non-Javadoc)
     * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#getCacheKeyId(java.lang.Object)
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
    public boolean insert(SessionImplementor session, Object key, Object value, Object version) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value, Object version) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean update(SessionImplementor session, Object key, Object value, Object currentVersion, Object previousVersion) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, Object currentVersion,
            Object previousVersion, SoftLock lock) {
        region.put(key, value);
        return true;
    }
}
