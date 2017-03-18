package com.sanluan.common.hibernate.redis.regions;

import java.util.Properties;

import org.hibernate.cache.spi.TimestampsRegion;

import com.sanluan.common.cache.redis.RedisClient;
import com.sanluan.common.hibernate.redis.ConfigurableRedisRegionFactory;
import com.sanluan.common.hibernate.redis.strategy.RedisAccessStrategyFactory;

/**
 *
 * RedisTimestampsRegion
 * 
 */
public class RedisTimestampsRegion extends RedisGeneralDataRegion implements TimestampsRegion {

    /**
     * @param accessStrategyFactory
     * @param redis
     * @param configurableRedisRegionFactory
     * @param regionName
     * @param props
     */
    public RedisTimestampsRegion(RedisAccessStrategyFactory accessStrategyFactory, RedisClient redis,
            ConfigurableRedisRegionFactory configurableRedisRegionFactory, String regionName, Properties props) {
        super(accessStrategyFactory, redis, configurableRedisRegionFactory, regionName, props);
    }
}
