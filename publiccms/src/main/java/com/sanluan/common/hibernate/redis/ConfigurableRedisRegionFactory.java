package com.sanluan.common.hibernate.redis;

import com.sanluan.common.cache.redis.RedisClient;
import com.sanluan.common.hibernate.redis.timestamper.CacheTimestamper;

public interface ConfigurableRedisRegionFactory {
    CacheTimestamper createCacheTimestamper(RedisClient redisClient, String cacheKey);
}
