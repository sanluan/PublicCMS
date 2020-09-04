package com.publiccms.common.cache;

import java.util.List;
import java.util.Properties;

/**
 *
 * CacheEntity
 * 
 * @param <K>
 * @param <V>
 * 
 */
public interface CacheEntity<K, V> {

    /**
     * @param region
     * @param properties
     * @return entity
     */
    CacheEntity<K, V> init(String region, Properties properties);

    /**
     * @param key
     * @param value
     * @param expiryInSeconds 
     */
    void put(K key, V value, Integer expiryInSeconds);

    /**
     * @param key
     * @param value
     * @return values list
     */
    List<V> put(K key, V value);

    /**
     * @param key
     * @return value
     */
    V remove(K key);

    /**
     * @param key
     * @return whether it is included
     */
    boolean contains(K key);

    /**
     * @param key
     * @return value
     */
    V get(K key);

    /**
     * @return values list
     */
    List<V> clear();

}
