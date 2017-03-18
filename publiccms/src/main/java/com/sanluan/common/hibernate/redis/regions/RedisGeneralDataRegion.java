package com.sanluan.common.hibernate.redis.regions;

import java.util.Properties;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.GeneralDataRegion;
import org.hibernate.engine.spi.SessionImplementor;

import com.sanluan.common.cache.redis.RedisClient;
import com.sanluan.common.hibernate.redis.ConfigurableRedisRegionFactory;
import com.sanluan.common.hibernate.redis.strategy.RedisAccessStrategyFactory;

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
    public RedisGeneralDataRegion(RedisAccessStrategyFactory accessStrategyFactory, RedisClient redisClient,
            ConfigurableRedisRegionFactory configurableRedisRegionFactory, String regionName, Properties props) {
        super(accessStrategyFactory, redisClient, configurableRedisRegionFactory, regionName, props);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.hibernate.cache.spi.GeneralDataRegion#get(org.hibernate.engine.spi.
     * SessionImplementor, java.lang.Object)
     */
    @Override
    public Object get(SessionImplementor session, Object key) throws CacheException {
        try {
            return redisClient.get(getName(), key);
        } catch (Exception ignored) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.hibernate.cache.spi.GeneralDataRegion#put(org.hibernate.engine.spi.
     * SessionImplementor, java.lang.Object, java.lang.Object)
     */
    @Override
    public void put(SessionImplementor session, Object key, Object value) throws CacheException {
        try {
            redisClient.set(getName(), key, value, getExpireInSeconds());
        } catch (Exception ignored) {
            log.warn("Fail to put. key=" + key, ignored);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.cache.spi.GeneralDataRegion#evict(java.lang.Object)
     */
    @Override
    public void evict(Object key) throws CacheException {
        try {
            redisClient.del(getName(), key);
        } catch (Exception ignored) {
            log.warn("Fail to evict. key=" + key, ignored);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.cache.spi.GeneralDataRegion#evictAll()
     */
    @Override
    public void evictAll() throws CacheException {
        try {
            super.redisClient.deleteRegion(getName());
        } catch (Exception ignored) {
            log.warn("Fail to evict all.", ignored);
        }
    }
}
