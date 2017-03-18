package com.sanluan.common.hibernate.redis;

import static com.sanluan.common.tools.RedisUtils.createJedisPool;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;

import java.util.Properties;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;

import com.sanluan.common.cache.redis.RedisClient;

/**
 *
 * RedisRegionFactory
 * 
 */
public class RedisRegionFactory extends AbstractRedisRegionFactory {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RedisRegionFactory(Properties props) {
        super(props);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.cache.spi.RegionFactory#start(org.hibernate.boot.spi.
     * SessionFactoryOptions, java.util.Properties)
     */
    @Override
    public void start(SessionFactoryOptions options, Properties properties) throws CacheException {
        log.debug("RedisRegionFactory is starting... ");

        this.options = options;
        try {
            if (redisClient == null) {
                String configurationResourceName = (String) properties
                        .get("com.sanluan.common.hibernate.redis.configurationResourceName");
                if (notEmpty(configurationResourceName)) {
                    Properties redisProperties = loadAllProperties(configurationResourceName);
                    this.redisClient = new RedisClient(createJedisPool(redisProperties));
                }
                this.cacheTimestamper = createCacheTimestamper(redisClient, RedisRegionFactory.class.getName());
            }
            log.info("RedisRegionFactory is started.");
        } catch (Exception e) {
            log.error("Fail to start RedisRegionFactory.", e);
            throw new CacheException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.cache.spi.RegionFactory#stop()
     */
    @Override
    public void stop() {
        if (redisClient == null)
            return;
        log.debug("Stopping RedisRegionFactory...");

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
