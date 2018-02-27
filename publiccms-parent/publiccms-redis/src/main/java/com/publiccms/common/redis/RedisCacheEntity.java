package com.publiccms.common.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.publiccms.common.base.Base;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.redis.serializer.BinarySerializer;
import com.publiccms.common.redis.serializer.StringSerializer;
import com.publiccms.common.tools.RedisUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * RedisCacheEntity
 *
 * @param <K>
 * @param <V>
 *
 */
public class RedisCacheEntity<K, V> implements CacheEntity<K, V>, java.io.Serializable, Base {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int size = 100;
    private JedisPool jedisPool;
    private String name;
    private byte[] byteName;
    private final static StringSerializer stringSerializer = new StringSerializer();
    private final BinarySerializer<K> keySerializer = new BinarySerializer<>();
    private final BinarySerializer<V> valueSerializer = new BinarySerializer<>();

    @Override
    public List<V> put(K key, V value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(getKey(key), valueSerializer.serialize(value));
        jedis.zadd(byteName, System.currentTimeMillis(), keySerializer.serialize(key));
        return clearCache(jedis);
    }

    @Override
    public void put(K key, V value, Integer expiry) {
        Jedis jedis = jedisPool.getResource();
        if (null == expiry) {
            jedis.set(getKey(key), valueSerializer.serialize(value));
            jedis.zadd(byteName, System.currentTimeMillis(), keySerializer.serialize(key));
        } else {
            jedis.setex(getKey(key), expiry, valueSerializer.serialize(value));
        }
        jedis.close();
    }

    @Override
    public V get(K key) {
        Jedis jedis = jedisPool.getResource();
        V value = valueSerializer.deserialize(jedis.get(getKey(key)));
        jedis.zadd(byteName, System.currentTimeMillis(), keySerializer.serialize(key));
        jedis.close();
        return value;
    }

    @Override
    public V remove(K key) {
        Jedis jedis = jedisPool.getResource();
        byte[] byteKey = getKey(key);
        V value = null;
        if (1 == jedis.zrem(byteName, keySerializer.serialize(key))) {
            value = valueSerializer.deserialize(jedis.get(byteKey));
            jedis.del(byteKey);
        }
        jedis.close();
        return value;
    }

    @Override
    public List<V> clear() {
        Jedis jedis = jedisPool.getResource();
        Set<byte[]> keyList = jedis.zrange(byteName, 0, -1);
        List<V> list = new ArrayList<>();
        if (0 < jedis.del(byteName)) {
            for (byte[] byteKey : keyList) {
                byte[] key = getKey(keySerializer.deserialize(byteKey));
                list.add(valueSerializer.deserialize(jedis.get(key)));
                jedis.del(key);
            }
        }
        jedis.close();
        return list;
    }

    @Override
    public long getDataSize() {
        Jedis jedis = jedisPool.getResource();
        long dataSize = jedis.zrevrange(byteName, 0, -1).size();
        jedis.close();
        return dataSize;
    }

    private List<V> clearCache(Jedis jedis) {
        List<V> list = null;
        if (size < jedis.zcount(byteName, 0, Long.MAX_VALUE)) {
            int helf = size / 2;
            Set<byte[]> keys = jedis.zrange(byteName, 0, helf);
            list = new ArrayList<>();
            for (byte[] byteKey : keys) {
                if (1 == jedis.zrem(byteName, byteKey)) {
                    byte[] key = getKey(keySerializer.deserialize(byteKey));
                    list.add(valueSerializer.deserialize(jedis.get(key)));
                    jedis.del(key);
                }
            }
        }
        jedis.close();
        return list;
    }

    /**
     *
     * @return
     *
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @param size
     *
     */
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public Map<K, V> getAll() {
        Jedis jedis = jedisPool.getResource();
        Set<byte[]> keySet = jedis.zrange(byteName, 0, -1);
        Map<K, V> map = new HashMap<>();
        for (byte[] byteKey : keySet) {
            K key = keySerializer.deserialize(byteKey);
            map.put(key, valueSerializer.deserialize(jedis.get(getKey(key))));
        }
        jedis.close();
        return map;
    }

    @Override
    public boolean contains(K key) {
        Jedis jedis = jedisPool.getResource();
        boolean exits = jedis.exists(getKey(key));
        jedis.close();
        return exits;
    }

    private byte[] getKey(K key) {
        return stringSerializer.serialize(name + DOT + key);
    }

    @Override
    public void init(String entityName, Integer cacheSize, Properties properties) {
        init(entityName, cacheSize, RedisUtils.createJedisPool(properties));
    }

    public void init(String entityName, Integer cacheSize, JedisPool pool) {
        this.name = entityName;
        this.byteName = stringSerializer.serialize(name);
        if (null != cacheSize) {
            this.size = cacheSize;
        }
        this.jedisPool = pool;
    }

}