package com.sanluan.common.cache;

import java.util.List;
import java.util.Map;

public interface CacheEntity<K, V> {

    public void put(K key, V value, Integer expiry);

    public List<V> put(K key, V value);

    public V remove(K key);

    public boolean contains(K key);

    public V get(K key);

    public List<V> clear();

    public long getDataSize();

    public Map<K, V> getAll();
}
