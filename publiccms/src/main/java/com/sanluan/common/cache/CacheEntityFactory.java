package com.sanluan.common.cache;

import static com.sanluan.common.tools.RedisUtils.createJedisPool;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;

import java.io.IOException;
import java.util.Properties;

import com.sanluan.common.base.Base;
import com.sanluan.common.cache.memory.MemoryCacheEntity;
import com.sanluan.common.cache.redis.RedisCacheEntity;

import redis.clients.jedis.JedisPool;

public class CacheEntityFactory extends Base {
    public static final String MEMORY_CACHE_ENTITY = "memory";
    public static final String RADIS_CACHE_ENTITY = "radis";
    private String defaultCacheEntity;
    private JedisPool jedisPool;
    private Properties properties;
    private int defaultSize = 100;

    public CacheEntityFactory(String configurationResourceName) throws IOException {
        this.properties = loadAllProperties(configurationResourceName);
        try {
            setDefaultSize(Integer.parseInt(properties.getProperty("cache.defaultSize")));
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
    }

    public synchronized JedisPool initJedisPool() {
        if (null == jedisPool) {
            jedisPool = createJedisPool(properties);
        }
        return jedisPool;
    }

    public <K, V> CacheEntity<K, V> createCacheEntity(String name, String type) {
        int size = defaultSize;
        try {
            size = Integer.valueOf(properties.getProperty("cache.size." + name));
        } catch (NumberFormatException e) {
        }
        CacheEntity<K, V> cacheEntity;
        if (MEMORY_CACHE_ENTITY.equals(type)) {
            cacheEntity = new MemoryCacheEntity<K, V>(size);
        } else {
            initJedisPool();
            cacheEntity = new RedisCacheEntity<K, V>(name, jedisPool, size);
        }
        return cacheEntity;
    }

    public <K, V> CacheEntity<K, V> createCacheEntity(String name) {
        return createCacheEntity(name, getDefaultCacheEntity());
    }

    public synchronized String getDefaultCacheEntity() {
        if (null == defaultCacheEntity) {
            defaultCacheEntity = properties.getProperty("cache.type");
        }
        return defaultCacheEntity;
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }
}
