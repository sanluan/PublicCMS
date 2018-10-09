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
     */
    public void init(String region, Properties properties);

    /**
     * @param key
     * @param value
     * @param expiryInSeconds 
     */
    public void put(K key, V value, Integer expiryInSeconds);

    /**
     * @param key
     * @param value
     * @return values list
     */
    public List<V> put(K key, V value);

    /**
     * @param key
     * @return value
     */
    public V remove(K key);

    /**
     * @param key
     * @return whether it is included
     */
    public boolean contains(K key);

    /**
     * @param key
     * @return value
     */
    public V get(K key);

    /**
     * @return values list
     */
    public List<V> clear();

}
