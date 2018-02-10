package com.publiccms.common.redis.hibernate;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.publiccms.common.redis.DatabaseRedisClient;
import com.publiccms.common.redis.hibernate.regions.RedisCollectionRegion;
import com.publiccms.common.redis.hibernate.regions.RedisEntityRegion;
import com.publiccms.common.redis.hibernate.regions.RedisNaturalIdRegion;
import com.publiccms.common.redis.hibernate.regions.RedisQueryResultsRegion;
import com.publiccms.common.redis.hibernate.regions.RedisTimestampsRegion;
import com.publiccms.common.redis.hibernate.strategy.RedisAccessStrategyFactory;
import com.publiccms.common.redis.hibernate.strategy.RedisAccessStrategyFactoryImpl;
import com.publiccms.common.redis.hibernate.timestamper.CacheTimestamper;
import com.publiccms.common.redis.hibernate.timestamper.Timestamper;

/**
 *
 * AbstractRedisRegionFactory
 * 
 */
public abstract class AbstractRedisRegionFactory implements RegionFactory, ConfigurableRedisRegionFactory {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected final Log log = LogFactory.getLog(getClass());
    protected final Properties props;
    protected SessionFactoryOptions options;
    protected final RedisAccessStrategyFactory accessStrategyFactory = new RedisAccessStrategyFactoryImpl();

    /**
     * {@link DatabaseRedisClient} instance.
     */
    protected volatile DatabaseRedisClient redisClient = null;
    protected CacheTimestamper cacheTimestamper = null;

    protected AbstractRedisRegionFactory(Properties props) {
        this.props = props;
    }

    @Override
    public CacheTimestamper createCacheTimestamper(DatabaseRedisClient client, String cacheKey) {
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
