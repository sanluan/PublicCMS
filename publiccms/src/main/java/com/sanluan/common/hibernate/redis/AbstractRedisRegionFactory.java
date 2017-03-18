package com.sanluan.common.hibernate.redis;

import java.util.Properties;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.TimestampsRegion;
import org.hibernate.cache.spi.access.AccessType;

import com.sanluan.common.base.Base;
import com.sanluan.common.cache.redis.RedisClient;
import com.sanluan.common.hibernate.redis.regions.RedisCollectionRegion;
import com.sanluan.common.hibernate.redis.regions.RedisEntityRegion;
import com.sanluan.common.hibernate.redis.regions.RedisNaturalIdRegion;
import com.sanluan.common.hibernate.redis.regions.RedisQueryResultsRegion;
import com.sanluan.common.hibernate.redis.regions.RedisTimestampsRegion;
import com.sanluan.common.hibernate.redis.strategy.RedisAccessStrategyFactory;
import com.sanluan.common.hibernate.redis.strategy.RedisAccessStrategyFactoryImpl;
import com.sanluan.common.hibernate.redis.timestamper.CacheTimestamper;
import com.sanluan.common.hibernate.redis.timestamper.Timestamper;

/**
 *
 * AbstractRedisRegionFactory
 * 
 */
public abstract class AbstractRedisRegionFactory extends Base implements RegionFactory, ConfigurableRedisRegionFactory {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected final Properties props;
    protected SessionFactoryOptions options;
    protected final RedisAccessStrategyFactory accessStrategyFactory = new RedisAccessStrategyFactoryImpl();

    /**
     * {@link RedisClient} instance.
     */
    protected volatile RedisClient redisClient = null;
    protected CacheTimestamper cacheTimestamper = null;

    protected AbstractRedisRegionFactory(Properties props) {
        this.props = props;
    }

    @Override
    public CacheTimestamper createCacheTimestamper(RedisClient redisClient, String cacheKey) {
        return new Timestamper();
    }

    @Override
    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }

    @Override
    public AccessType getDefaultAccessType() {
        return AccessType.READ_WRITE;
    }

    @Override
    public long nextTimestamp() {
        return cacheTimestamper.next();
    }

    @Override
    public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata)
            throws CacheException {
        return new RedisEntityRegion(accessStrategyFactory, redisClient, this, regionName, options, metadata, properties);
    }

    @Override
    public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata)
            throws CacheException {
        return new RedisNaturalIdRegion(accessStrategyFactory, redisClient, this, regionName, options, metadata, properties);
    }

    @Override
    public CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata)
            throws CacheException {
        return new RedisCollectionRegion(accessStrategyFactory, redisClient, this, regionName, options, metadata, properties);
    }

    @Override
    public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
        return new RedisQueryResultsRegion(accessStrategyFactory, redisClient, this, regionName, properties);
    }

    @Override
    public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
        return new RedisTimestampsRegion(accessStrategyFactory, redisClient, this, regionName, properties);
    }
}
