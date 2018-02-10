package com.publiccms.common.redis.hibernate.strategy;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionImplementor;

import com.publiccms.common.redis.hibernate.regions.RedisTransactionalDataRegion;

/**
 *
 * AbstractRedisAccessStrategy
 * @param <T> 
 * 
 */
public abstract class AbstractRedisAccessStrategy<T extends RedisTransactionalDataRegion> {

    protected final T region;
    protected final SessionFactoryOptions options;

    AbstractRedisAccessStrategy(T region, SessionFactoryOptions options) {
        this.region = region;
        this.options = options;
    }

    protected SessionFactoryOptions options() {
        return this.options;
    }

    /**
     * @param session
     * @param key
     * @param txTimestamp
     * @return
     */
    public Object get(SessionImplementor session, Object key, long txTimestamp) {
        return region.get(key);
    }

    /**
     * @param session
     * @param key
     * @param value
     * @param txTimestamp
     * @param version
     * @return
     */
    public final boolean putFromLoad(SessionImplementor session, Object key, Object value, long txTimestamp, Object version) {
        return putFromLoad(session, key, value, txTimestamp, version, options.isMinimalPutsEnabled());
    }

    /**
     * @param session
     * @param key
     * @param value
     * @param txTimestamp
     * @param version
     * @param minimalPutOverride
     * @return
     * @throws CacheException
     */
    public abstract boolean putFromLoad(SessionImplementor session, Object key, Object value, long txTimestamp, Object version,
            boolean minimalPutOverride) throws CacheException;

    /**
     * Region locks are not supported
     * 
     * @return
     */
    public final SoftLock lockRegion() {
        return null;
    }

    /**
     * @param lock
     */
    public final void unlockRegion(SoftLock lock) {
        region.clear();
    }

    /**
     * @param session
     * @param key
     * @throws CacheException
     */
    public void remove(SessionImplementor session, Object key) throws CacheException {
        region.remove(key);
    }

    /**
     * 
     */
    public final void removeAll() {
        region.clear();
    }

    /**
     * @param key
     */
    public final void evict(Object key) {
        region.remove(key);
    }

    /**
     * 
     */
    public final void evictAll() {
        region.clear();
    }

    /**
     * @param session
     * @param key
     * @param version
     * @return
     * @throws CacheException
     */
    public SoftLock lockItem(SessionImplementor session, Object key, Object version) throws CacheException {
        region.remove(key);
        return null;
    }

    /**
     * @param session
     * @param key
     * @param lock
     * @throws CacheException
     */
    public void unlockItem(SessionImplementor session, Object key, SoftLock lock) throws CacheException {
        region.remove(key);
    }
}
