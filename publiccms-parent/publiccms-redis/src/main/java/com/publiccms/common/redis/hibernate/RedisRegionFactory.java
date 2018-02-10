package com.publiccms.common.redis.hibernate;

import java.util.Properties;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.publiccms.common.redis.DatabaseRedisClient;
import com.publiccms.common.tools.RedisUtils;

/**
 * Redis领域工厂
 * 
 * Redis Region Factory
 * 
 */
public class RedisRegionFactory extends AbstractRedisRegionFactory {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param props
     */
    public RedisRegionFactory(Properties props) {
        super(props);
    }

    @Override
    public void start(SessionFactoryOptions option, Properties properties) throws CacheException {
        log.debug("RedisRegionFactory is starting... ");

        this.options = option;
        try {
            if (redisClient == null) {
                String configurationResourceName = (String) properties
                        .get("hibernate.redis.configurationResourceName");
                if (null != configurationResourceName) {
                    Properties redisProperties = PropertiesLoaderUtils.loadAllProperties(configurationResourceName);
                    this.redisClient = new DatabaseRedisClient(RedisUtils.createJedisPool(redisProperties));
                }
                this.cacheTimestamper = createCacheTimestamper(redisClient, RedisRegionFactory.class.getName());
            }
            log.info("RedisRegionFactory is started.");
        } catch (Exception e) {
            log.error("Fail to start RedisRegionFactory.", e);
            throw new CacheException(e);
        }
    }

    @Override
    public void stop() {
        if (redisClient == null)
            return;

        try {
            redisClient.shutdown();
            redisClient = null;
            cacheTimestamper = null;
            log.info("RedisRegionFactory is stopped.");
        } catch (Exception ignored) {
            log.error("Fail to stop RedisRegionFactory.", ignored);
        }
    }
}
