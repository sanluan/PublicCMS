package com.sanluan.common.hibernate.redis.regions;

import java.util.Properties;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.TransactionalDataRegion;

import com.sanluan.common.cache.redis.RedisClient;
import com.sanluan.common.hibernate.redis.ConfigurableRedisRegionFactory;
import com.sanluan.common.hibernate.redis.strategy.RedisAccessStrategyFactory;

/**
 *
 * RedisTransactionalDataRegion
 * 
 */
public class RedisTransactionalDataRegion extends RedisDataRegion implements TransactionalDataRegion {

    protected final SessionFactoryOptions options;
    protected final CacheDataDescription metadata;

    /**
     * @param accessStrategyFactory
     * @param redis
     * @param configurableRedisRegionFactory
     * @param regionName
     * @param options
     * @param metadata
     * @param props
     */
    public RedisTransactionalDataRegion(RedisAccessStrategyFactory accessStrategyFactory, RedisClient redis,
            ConfigurableRedisRegionFactory configurableRedisRegionFactory, String regionName, SessionFactoryOptions options,
            CacheDataDescription metadata, Properties props) {
        super(accessStrategyFactory, redis, configurableRedisRegionFactory, regionName, props);
        this.options = options;
        this.metadata = metadata;
    }

    @Override
    public boolean isTransactionAware() {
        return false;
    }

    @Override
    public CacheDataDescription getCacheDataDescription() {
        return metadata;
    }

    /**
     * @param key
     * @return
     */
    public Object get(Object key) {
        try {
            return redisClient.get(getName(), key);
        } catch (Exception ignored) {
            log.warn("Fail to get cache item... key=" + key, ignored);
            return null;
        }
    }

    /**
     * @param key
     * @param value
     */
    public void put(Object key, Object value) {
        try {
            redisClient.set(getName(), key, value, getExpireInSeconds());
        } catch (Exception ignored) {
            log.warn("Fail to put cache item... key=" + key, ignored);
        }
    }

    /**
     * @param key
     * @throws CacheException
     */
    public void remove(Object key) throws CacheException {
        try {
            redisClient.del(getName(), key);
        } catch (Exception ignored) {
            log.warn("Fail to remove cache item... key=" + key, ignored);
        }
    }

    /**
     * 
     */
    public void clear() {
        try {
            redisClient.deleteRegion(getName());
        } catch (Exception ignored) {
            log.warn("Fail to clear region... name=" + getName(), ignored);
        }
    }

    public SessionFactoryOptions getOptions() {
        return options;
    }

    public CacheDataDescription getMetadata() {
        return metadata;
    }
}
