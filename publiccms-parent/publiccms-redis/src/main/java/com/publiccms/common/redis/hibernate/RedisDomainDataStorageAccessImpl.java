package com.publiccms.common.redis.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.spi.support.DomainDataStorageAccess;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import com.publiccms.common.redis.RedisClient;
import com.publiccms.common.redis.RedisCacheEntity;

/**
 * Redis 访问实现
 * 
 * Redis Storage Access
 * 
 */
public class RedisDomainDataStorageAccessImpl implements DomainDataStorageAccess {
    protected final Log log = LogFactory.getLog(getClass());
    protected final RedisClient redisClient;
    protected final RedisCacheEntity<Object, Object> cache;

    public RedisDomainDataStorageAccessImpl(RedisClient redisClient, RedisCacheEntity<Object, Object> cache) {
        this.redisClient = redisClient;
        this.cache = cache;
    }

    @Override
    public boolean contains(Object key) {
        return cache.contains(key.toString());
    }

    @Override
    public Object getFromCache(Object key, SharedSessionContractImplementor session) {
        return cache.get(key);
    }

    @Override
    public void putIntoCache(Object key, Object value, SharedSessionContractImplementor session) {
        cache.put(key, value);
    }

    @Override
    public void evictData(Object key) {
        cache.remove(key);
    }

    @Override
    public void evictData() {
        cache.clear();
    }

    @Override
    public void release() {
        redisClient.removeRegion(cache.getRegion());
    }
}
