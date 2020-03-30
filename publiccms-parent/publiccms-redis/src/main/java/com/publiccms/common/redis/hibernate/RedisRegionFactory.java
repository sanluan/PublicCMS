package com.publiccms.common.redis.hibernate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.cfg.spi.DomainDataRegionBuildingContext;
import org.hibernate.cache.cfg.spi.DomainDataRegionConfig;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.DomainDataRegion;
import org.hibernate.cache.spi.SecondLevelCacheLogger;
import org.hibernate.cache.spi.support.DomainDataRegionImpl;
import org.hibernate.cache.spi.support.DomainDataStorageAccess;
import org.hibernate.cache.spi.support.RegionFactoryTemplate;
import org.hibernate.cache.spi.support.RegionNameQualifier;
import org.hibernate.cache.spi.support.StorageAccess;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.publiccms.common.redis.RedisClient;
import com.publiccms.common.redis.RedisCacheEntity;
import com.publiccms.common.tools.RedisUtils;

/**
 * Redis领域工厂
 * 
 * Redis Region Factory
 * 
 */
public class RedisRegionFactory extends RegionFactoryTemplate {
    protected final Log log = LogFactory.getLog(getClass());
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final CacheKeysFactory cacheKeysFactory;
    /**
     * {@link RedisClient} instance.
     */
    protected volatile RedisClient redisClient;

    public RedisRegionFactory() {
        this(DefaultCacheKeysFactory.INSTANCE);
    }

    public RedisRegionFactory(CacheKeysFactory cacheKeysFactory) {
        this.cacheKeysFactory = cacheKeysFactory;
    }

    @Override
    protected CacheKeysFactory getImplicitCacheKeysFactory() {
        return cacheKeysFactory;
    }

    @Override
    public DomainDataRegion buildDomainDataRegion(DomainDataRegionConfig regionConfig,
            DomainDataRegionBuildingContext buildingContext) {
        return new DomainDataRegionImpl(regionConfig, this, createDomainDataStorageAccess(regionConfig, buildingContext),
                cacheKeysFactory, buildingContext);
    }

    @Override
    protected DomainDataStorageAccess createDomainDataStorageAccess(DomainDataRegionConfig regionConfig,
            DomainDataRegionBuildingContext buildingContext) {
        return new RedisDomainDataStorageAccessImpl(redisClient,
                getOrCreateCache(regionConfig.getRegionName(), buildingContext.getSessionFactory()));
    }

    @Override
    protected StorageAccess createQueryResultsRegionStorageAccess(String regionName, SessionFactoryImplementor sessionFactory) {
        String defaultedRegionName = defaultRegionName(regionName, sessionFactory, DEFAULT_QUERY_RESULTS_REGION_UNQUALIFIED_NAME,
                LEGACY_QUERY_RESULTS_REGION_UNQUALIFIED_NAMES);
        return new RedisDomainDataStorageAccessImpl(redisClient, getOrCreateCache(defaultedRegionName, sessionFactory));
    }

    @Override
    protected StorageAccess createTimestampsRegionStorageAccess(String regionName, SessionFactoryImplementor sessionFactory) {
        String defaultedRegionName = defaultRegionName(regionName, sessionFactory,
                DEFAULT_UPDATE_TIMESTAMPS_REGION_UNQUALIFIED_NAME, LEGACY_UPDATE_TIMESTAMPS_REGION_UNQUALIFIED_NAMES);
        return new RedisDomainDataStorageAccessImpl(redisClient, getOrCreateCache(defaultedRegionName, sessionFactory));
    }

    protected final String defaultRegionName(String regionName, SessionFactoryImplementor sessionFactory,
            String defaultRegionName, List<String> legacyDefaultRegionNames) {
        if (defaultRegionName.equals(regionName) && !cacheExists(regionName, sessionFactory)) {

            for (String legacyDefaultRegionName : legacyDefaultRegionNames) {
                if (cacheExists(legacyDefaultRegionName, sessionFactory)) {
                    SecondLevelCacheLogger.INSTANCE.usingLegacyCacheName(defaultRegionName, legacyDefaultRegionName);
                    return legacyDefaultRegionName;
                }
            }
        }

        return regionName;
    }

    protected RedisCacheEntity<Object, Object> getOrCreateCache(String unqualifiedRegionName,
            SessionFactoryImplementor sessionFactory) {
        verifyStarted();
        assert !RegionNameQualifier.INSTANCE.isQualified(unqualifiedRegionName, sessionFactory.getSessionFactoryOptions());

        final String qualifiedRegionName = RegionNameQualifier.INSTANCE.qualify(unqualifiedRegionName,
                sessionFactory.getSessionFactoryOptions());
        return redisClient.createOrGetCache(qualifiedRegionName);
    }

    protected boolean cacheExists(String unqualifiedRegionName, SessionFactoryImplementor sessionFactory) {
        final String qualifiedRegionName = RegionNameQualifier.INSTANCE.qualify(unqualifiedRegionName,
                sessionFactory.getSessionFactoryOptions());
        return null != redisClient.createOrGetCache(qualifiedRegionName);
    }

    protected boolean isStarted() {
        return super.isStarted() && null != redisClient;
    }

    @Override
    protected void prepareForUse(SessionFactoryOptions settings, @SuppressWarnings("rawtypes") Map configValues) {
        try {
            this.redisClient = resolveRedisClient(configValues);
            if (this.redisClient == null) {
                throw new CacheException("Could not start Redis Client");
            }
            log.info("RedisRegionFactory is started.");
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    protected RedisClient resolveRedisClient(@SuppressWarnings("rawtypes") Map configValues) throws IOException {
        String configurationResourceName = (String) configValues.get("hibernate.redis.configurationResourceName");
        if (null != configurationResourceName) {
            Properties redisProperties = PropertiesLoaderUtils.loadAllProperties(configurationResourceName);
            return new RedisClient(RedisUtils.createOrGetJedisPool(redisProperties));
        } else {
            return null;
        }
    }

    @Override
    protected void releaseFromUse() {
        if (null != redisClient) {
            try {
                redisClient.shutdown();
                redisClient = null;
                log.info("RedisRegionFactory is stopped.");
            } catch (Exception ignored) {
                log.error("Fail to stop RedisRegionFactory.", ignored);
            }
        }
    }
}
