package com.publiccms.common.redis.hibernate;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.publiccms.common.redis.RedisClient;
import com.publiccms.common.tools.RedisUtils;

/**
 *
 * SingletonRedisRegionFactory
 * 
 */
public class SingletonRedisRegionFactory extends RedisRegionFactory {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final AtomicInteger referenceCount = new AtomicInteger();

    protected RedisClient resolveRedisClient(@SuppressWarnings("rawtypes") Map configValues) throws IOException {
        String configurationResourceName = (String) configValues.get("hibernate.redis.configurationResourceName");
        if (null != configurationResourceName) {
            try {
                referenceCount.incrementAndGet();
                Properties redisProperties = PropertiesLoaderUtils.loadAllProperties(configurationResourceName);
                return new RedisClient(RedisUtils.createOrGetJedisPool(redisProperties));
            } catch (RuntimeException e) {
                referenceCount.decrementAndGet();
                throw e;
            }
        } else {
            return null;
        }
    }

    @Override
    protected void releaseFromUse() {
        if (0 == referenceCount.decrementAndGet()) {
            super.releaseFromUse();
        }
    }
}
