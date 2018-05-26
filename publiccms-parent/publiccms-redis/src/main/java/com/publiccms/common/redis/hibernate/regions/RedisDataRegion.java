package com.publiccms.common.redis.hibernate.regions;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.Region;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.redis.DatabaseRedisClient;
import com.publiccms.common.redis.hibernate.ConfigurableRedisRegionFactory;
import com.publiccms.common.redis.hibernate.strategy.RedisAccessStrategyFactory;
import com.publiccms.common.redis.hibernate.timestamper.CacheTimestamper;

/**
 *
 * RedisDataRegion
 * 
 */
public abstract class RedisDataRegion implements Region {
    protected final Log log = LogFactory.getLog(getClass());

    private static final String EXPIRE_IN_SECONDS = "redis.expiryInSeconds";
    protected final RedisAccessStrategyFactory accessStrategyFactory;

    private final String regionName;

    protected final DatabaseRedisClient redisClient;

    private final CacheTimestamper cacheTimestamper;

    private final int expireInSeconds;

    /**
     * @param accessStrategyFactory
     * @param redisClient
     * @param configurableRedisRegionFactory
     * @param regionName
     * @param props
     */
    public RedisDataRegion(RedisAccessStrategyFactory accessStrategyFactory, DatabaseRedisClient redisClient,
            ConfigurableRedisRegionFactory configurableRedisRegionFactory, String regionName, Properties props) {
        this.accessStrategyFactory = accessStrategyFactory;
        this.redisClient = redisClient;
        this.regionName = regionName;
        this.expireInSeconds = Integer
                .valueOf(props.getProperty(EXPIRE_IN_SECONDS + CommonConstants.DOT + regionName, getDefaultExpireInSeconds(props)));
        this.cacheTimestamper = configurableRedisRegionFactory.createCacheTimestamper(redisClient, regionName);
    }

    /**
     * Region regionName
     *
     * @return region regionName
     */
    public String getName() {
        return regionName;
    }

    /**
     * delete region
     */
    @Override
    public void destroy() throws CacheException {
    }

    @Override
    public boolean contains(Object key) {
        try {
            return redisClient.exists(regionName, key);
        } catch (Exception ignored) {
            log.warn("Fail to exists key. key=" + key, ignored);
            return false;
        }
    }

    @Override
    public long getSizeInMemory() {
        try {
            long sizeInMemory = redisClient.dbSize();
            return sizeInMemory;
        } catch (Exception ignored) {
            log.warn("Fail to get size in memory.", ignored);
            return 0;
        }
    }

    @Override
    public long getElementCountInMemory() {
        return redisClient.getDataSize(regionName);
    }

    @Override
    public long getElementCountOnDisk() {
        return -1;
    }

    @Override
    public Map<?, ?> toMap() {
        return redisClient.getAll(regionName);
    }

    @Override
    public long nextTimestamp() {
        return cacheTimestamper.next();
    }

    /**
     * @return
     */
    public int getExpireInSeconds() {
        return expireInSeconds;
    }

    @Override
    public int getTimeout() {
        return 0;
    }

    /**
     * @return
     */
    public DatabaseRedisClient getRedisClient() {
        return redisClient;
    }

    private static String getDefaultExpireInSeconds(final Properties props) {
        return props.getProperty(EXPIRE_IN_SECONDS, String.valueOf(DatabaseRedisClient.DEFAULT_EXPIRY_IN_SECONDS));
    }
}
