package com.publiccms.common.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * MemoryCacheEntity
 * 
 * @param <K>
 * @param <V>
 * 
 */
public class MemoryCacheEntity<K, V> implements CacheEntity<K, V>, java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int size = 300;
    protected final Log log = LogFactory.getLog(getClass());
    private LinkedHashMap<K, CacheValue<V>> cachedMap = new LinkedHashMap<>(16, 0.75f, true);
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public List<V> put(K key, V value) {
        lock.writeLock().lock();
        try {
            cachedMap.put(key, new CacheValue<V>(value));
            return clearCache();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void put(K key, V value, Integer expiryInSeconds) {
        lock.writeLock().lock();
        try {
            CacheValue<V> cacheValue = new CacheValue<V>(value);
            if (null != expiryInSeconds) {
                cacheValue.setExpiryDate(System.currentTimeMillis() + (expiryInSeconds * 1000));
            }
            cachedMap.put(key, cacheValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V get(K key) {
        lock.readLock().lock();
        try {
            CacheValue<V> cacheValue = cachedMap.get(key);
            if (null == cacheValue) {
                return null;
            } else {
                if (null == cacheValue.getExpiryDate()) {
                    return cacheValue.getValue();
                } else if (System.currentTimeMillis() < cacheValue.getExpiryDate()) {
                    return cacheValue.getValue();
                } else {
                    cachedMap.remove(key);
                    return null;
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<V> clear() {
        lock.writeLock().lock();
        try {
            List<V> list = cachedMap.values().stream().map(m -> m.getValue()).collect(Collectors.toList());
            cachedMap.clear();
            return list;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V remove(K key) {
        lock.writeLock().lock();
        try {
            CacheValue<V> cacheValue = cachedMap.remove(key);
            if (null == cacheValue
                    || null != cacheValue.getExpiryDate() && System.currentTimeMillis() > cacheValue.getExpiryDate()) {
                return null;
            } else {
                return cacheValue.getValue();
            }

        } finally {
            lock.writeLock().unlock();
        }
    }

    private List<V> clearCache() {
        List<V> list = null;
        if (size < cachedMap.size()) {
            Iterator<K> iterator = cachedMap.keySet().iterator();
            List<K> keyList = new ArrayList<>();
            for (int i = 0; i < size / 2; i++) {
                keyList.add(iterator.next());
            }
            list = new ArrayList<>();
            for (K key : keyList) {
                list.add(cachedMap.remove(key).getValue());
            }
        }
        return list;
    }

    /**
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean contains(K key) {
        lock.readLock().lock();
        try {
            return cachedMap.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void init(String region, Properties properties) {
        try {
            this.size = Integer.parseInt(properties.getProperty("cache.defaultSize"));
        } catch (NumberFormatException e) {
            log.warn(e);
        }
    }
}
