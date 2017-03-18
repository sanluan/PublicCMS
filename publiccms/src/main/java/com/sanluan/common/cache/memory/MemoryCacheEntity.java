package com.sanluan.common.cache.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sanluan.common.cache.CacheEntity;

public class MemoryCacheEntity<K, V> implements CacheEntity<K, V>, java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int size;
    private LinkedHashMap<K, V> cachedMap = new LinkedHashMap<K, V>(16, 0.75f, true);
    private ReentrantReadWriteLock  lock = new ReentrantReadWriteLock ();

    public MemoryCacheEntity(int size) {
        this.size = size;
    }

    @Override
    public List<V> put(K key, V value) {
        lock.writeLock().lock();
        try {
            cachedMap.put(key, value);
            return clearCache();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void put(K key, V value, Integer expiry) {
        lock.writeLock().lock();
        try {
            cachedMap.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V get(K key) {
        lock.readLock().lock();
        try {
            return cachedMap.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<V> clear() {
        lock.writeLock().lock();
        try {
            Collection<V> values = cachedMap.values();
            List<V> list = new ArrayList<V>();
            if(!values.isEmpty()){
                list.addAll(values);
            }
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
            return cachedMap.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private List<V> clearCache() {
        List<V> list = null;
        if (size < cachedMap.size()) {
            Iterator<K> iterator = cachedMap.keySet().iterator();
            List<K> keyList = new ArrayList<K>();
            for (int i = 0; i < size / 2; i++) {
                keyList.add(iterator.next());
            }
            list = new ArrayList<V>();
            for (K key : keyList) {
                list.add(cachedMap.remove(key));
            }
        }
        return list;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public long getDataSize() {
        return cachedMap.size();
    }

    @Override
    public Map<K, V> getAll() {
        return cachedMap;
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
}
