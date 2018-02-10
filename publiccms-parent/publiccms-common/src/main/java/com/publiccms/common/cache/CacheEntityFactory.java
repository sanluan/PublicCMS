package com.publiccms.common.cache;


import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 *
 * CacheEntityFactory
 * 
 */
public class CacheEntityFactory {
    /**
     * 
     */
    public static final String MEMORY_CACHE_ENTITY = "memory";
    protected final Log log = LogFactory.getLog(getClass());
    private String defaultCacheEntity;
    private Properties properties;
    private int defaultSize = 100;

    /**
     * @param configurationResourceName
     * @throws IOException
     */
    public CacheEntityFactory(String configurationResourceName) throws IOException {
        this.properties = PropertiesLoaderUtils.loadAllProperties(configurationResourceName);
        this.defaultCacheEntity = properties.getProperty("cache.type");
        try {
            setDefaultSize(Integer.parseInt(properties.getProperty("cache.defaultSize")));
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * @param name
     * @param type
     * @return cache entity
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public <K, V> CacheEntity<K, V> createCacheEntity(String name, String type)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        int size = defaultSize;
        try {
            size = Integer.valueOf(properties.getProperty("cache.size." + name));
        } catch (NumberFormatException e) {
        }
        CacheEntity<K, V> cacheEntity;
        if (MEMORY_CACHE_ENTITY.equals(type)) {
            cacheEntity = new MemoryCacheEntity<>();
        } else {
            @SuppressWarnings("unchecked")
            Class<CacheEntity<K, V>> c = (Class<CacheEntity<K, V>>) Class.forName(type);
            cacheEntity = c.newInstance();
        }
        cacheEntity.init(name, size, properties);
        return cacheEntity;
    }

    /**
     * @param name
     * @return cache entity
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public <K, V> CacheEntity<K, V> createCacheEntity(String name)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return createCacheEntity(name, defaultCacheEntity);
    }

    /**
     * @param defaultSize
     */
    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }
}
