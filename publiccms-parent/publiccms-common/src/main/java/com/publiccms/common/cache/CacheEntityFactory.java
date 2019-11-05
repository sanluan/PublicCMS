package com.publiccms.common.cache;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

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
    private String defaultCacheEntity;
    private Properties properties;

    /**
     * @param configurationResourceName
     * @throws IOException
     */
    public CacheEntityFactory(String configurationResourceName) throws IOException {
        this.properties = PropertiesLoaderUtils.loadAllProperties(configurationResourceName);
        this.defaultCacheEntity = properties.getProperty("cache.type");
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
        CacheEntity<K, V> cacheEntity;
        if (MEMORY_CACHE_ENTITY.equals(type)) {
            cacheEntity = new MemoryCacheEntity<>();
        } else {
            @SuppressWarnings("unchecked")
            Class<CacheEntity<K, V>> c = (Class<CacheEntity<K, V>>) Class.forName(type);
            try {
                cacheEntity = c.getDeclaredConstructor().newInstance();
            } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new ClassNotFoundException(type);
            }
        }
        cacheEntity.init(name, properties);
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
}
