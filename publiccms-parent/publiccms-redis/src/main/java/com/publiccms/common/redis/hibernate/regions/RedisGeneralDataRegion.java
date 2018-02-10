package com.publiccms.common.redis.hibernate.regions;

import java.util.Properties;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.GeneralDataRegion;
import org.hibernate.engine.spi.SessionImplementor;

import com.publiccms.common.redis.DatabaseRedisClient;
import com.publiccms.common.redis.hibernate.ConfigurableRedisRegionFactory;
import com.publiccms.common.redis.hibernate.strategy.RedisAccessStrategyFactory;

/**
 *
 * RedisGeneralDataRegion
 * 
 */
public class RedisGeneralDataRegion extends RedisDataRegion implements GeneralDataRegion {

    /**
     * @param accessStrategyFactory
     * @param redisClient
     * @param configurableRedisRegionFactory
     * @param regionName
     * @param props
     */
    public RedisGeneralDataRegion(RedisAccessStrategyFactory accessStrategyFactory, DatabaseRedisClient redisClient,
            ConfigurableRedisRegionFactory configurableRedisRegionFactory, String regionName, Properties props) {
        super(accessStrategyFactory, redisClient, configurableRedisRegionFactory, regionName, props);
    }

    @Override
    public Object get(SessionImplementor session, Object key) throws CacheException {
        try {
            return redisClient.get(getName(), key);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public void put(SessionImplementor session, Object key, Object value) throws CacheException {
        try {
            redisClient.set(getName(), key, value, getExpireInSeconds());
        } catch (Exception ignored) {
            log.warn("Fail to put. key=" + key, ignored);
        }
    }

    @Override
    public void evict(Object key) throws CacheException {
        try {
            redisClient.del(getName(), key);
        } catch (Exception ignored) {
            log.warn("Fail to evict. key=" + key, ignored);
        }
    }

    @Override
    public void evictAll() throws CacheException {
        try {
            super.redisClient.deleteRegion(getName());
        } catch (Exception ignored) {
            log.warn("Fail to evict all.", ignored);
        }
    }
}
