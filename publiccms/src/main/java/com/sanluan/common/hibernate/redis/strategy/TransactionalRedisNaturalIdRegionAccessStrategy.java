package com.sanluan.common.hibernate.redis.strategy;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;

import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;

import com.sanluan.common.cache.redis.RedisClient;
import com.sanluan.common.hibernate.redis.regions.RedisNaturalIdRegion;

public class TransactionalRedisNaturalIdRegionAccessStrategy extends AbstractRedisAccessStrategy<RedisNaturalIdRegion>
        implements NaturalIdRegionAccessStrategy {

    private final RedisClient redisClient;

    public TransactionalRedisNaturalIdRegionAccessStrategy(RedisNaturalIdRegion region, SessionFactoryOptions options) {
        super(region, options);
        this.redisClient = region.getRedisClient();
    }

    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value) {
        return false;
    }

    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, SoftLock lock) {
        return false;
    }

    @Override
    public Object get(SessionImplementor session, Object key, long txTimestamp) {
        return region.get(key);
    }

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

    @Override
    public boolean insert(SessionImplementor session, Object key, Object value) {
        region.put(key, value);
        return true;
    }

    @Override
    public SoftLock lockItem(SessionImplementor session, Object key, Object version) {
        region.remove(key);
        return null;
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
    public void remove(SessionImplementor session, Object key) {
        region.remove(key);
    }

    @Override
    public void unlockItem(SessionImplementor session, Object key, SoftLock lock) {
        region.remove(key);
    }

    @Override
    public boolean update(SessionImplementor session, Object key, Object value) {
        region.put(key, value);
        return true;
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }
}
