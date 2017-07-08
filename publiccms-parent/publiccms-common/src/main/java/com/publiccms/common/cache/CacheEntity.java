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
     * @return
     */
    public List<V> put(K key, V value);

    /**
     * @param key
     * @return
     */
    public V remove(K key);

    /**
     * @param key
     * @return
     */
    public boolean contains(K key);

    /**
     * @param key
     * @return
     */
    public V get(K key);

    /**
     * @return
     */
    public List<V> clear();

    /**
     * @return
     */
    public long getDataSize();

    /**
     * @return
     */
    public Map<K, V> getAll();
}
