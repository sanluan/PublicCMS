package com.publiccms.common.cache;

import java.util.List;
import java.util.Map;
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
     * @param name
     * @param size
     * @param properties
     */
    public void init(String name, Integer size, Properties properties);

    /**
     * @param key
     * @param value
     * @param expiry
     */
    public void put(K key, V value, Integer expiry);

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

    /**
     * @return datasize
     */
    public long getDataSize();

    /**
     * @return values map
     */
    public Map<K, V> getAll();
}
