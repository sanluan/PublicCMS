package com.publiccms.common.redis.hibernate.strategy;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;

import com.publiccms.common.redis.DatabaseRedisClient;
import com.publiccms.common.redis.hibernate.regions.RedisEntityRegion;

/**
 *
 * TransactionalRedisEntityRegionAccessStrategy
 * 
 */
public class TransactionalRedisEntityRegionAccessStrategy extends AbstractRedisAccessStrategy<RedisEntityRegion>
        implements EntityRegionAccessStrategy {

    private final DatabaseRedisClient redisclient;

    /**
     * @param region
     * @param options
     */
    public TransactionalRedisEntityRegionAccessStrategy(RedisEntityRegion region, SessionFactoryOptions options) {
        super(region, options);
        this.redisclient = region.getRedisClient();
    }

    @Override
    public Object generateCacheKey(Object id, EntityPersister persister, SessionFactoryImplementor factory,
            String tenantIdentifier) {
        return DefaultCacheKeysFactory.staticCreateEntityKey(id, persister, factory, tenantIdentifier);
    }

    @Override
    public Object getCacheKeyId(Object cacheKey) {
        return DefaultCacheKeysFactory.staticGetEntityId(cacheKey);
    }

    @Override
    public EntityRegion getRegion() {
        return region;
    }

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
        region.remove(key);
        return null;
    }

    @Override
    public void unlockItem(SessionImplementor session, Object key, SoftLock lock) {
        region.remove(key);
    }

    @Override
    public boolean insert(SessionImplementor session, Object key, Object value, Object version) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value, Object version) {
        return false;
    }

    @Override
    public boolean update(SessionImplementor session, Object key, Object value, Object currentVersion, Object previousVersion) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, Object currentVersion,
            Object previousVersion, SoftLock lock) {
        return false;
    }

    @Override
    public void remove(SessionImplementor session, Object key) {
        region.remove(key);
    }

    /**
     * @return
     */
    public DatabaseRedisClient getRedisclient() {
        return redisclient;
    }
}
