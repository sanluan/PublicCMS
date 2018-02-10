package com.publiccms.common.redis.hibernate.regions;

import java.util.Properties;

import org.hibernate.cache.spi.QueryResultsRegion;

import com.publiccms.common.redis.DatabaseRedisClient;
import com.publiccms.common.redis.hibernate.ConfigurableRedisRegionFactory;
import com.publiccms.common.redis.hibernate.strategy.RedisAccessStrategyFactory;

/**
 *
 * RedisQueryResultsRegion
 * 
 */
public class RedisQueryResultsRegion extends RedisGeneralDataRegion implements QueryResultsRegion {

    /**
     * @param accessStrategyFactory
     * @param redis
     * @param configurableRedisRegionFactory
     * @param regionName
     * @param props
     */
    public RedisQueryResultsRegion(RedisAccessStrategyFactory accessStrategyFactory, DatabaseRedisClient redis,
            ConfigurableRedisRegionFactory configurableRedisRegionFactory, String regionName, Properties props) {
        super(accessStrategyFactory, redis, configurableRedisRegionFactory, regionName, props);
    }
}
