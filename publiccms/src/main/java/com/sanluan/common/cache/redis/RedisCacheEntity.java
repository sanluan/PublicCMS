package com.sanluan.common.cache.redis;

import static org.apache.commons.lang3.ArrayUtils.addAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sanluan.common.base.Base;
import com.sanluan.common.cache.CacheEntity;
import com.sanluan.common.cache.redis.serializer.BinarySerializer;
import com.sanluan.common.cache.redis.serializer.StringSerializer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCacheEntity<K, V> extends Base implements CacheEntity<K, V>, java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int size = 100;
    private JedisPool jedisPool;
    private byte[] bytePrefix;
    private byte[] byteCount;
    private final static StringSerializer regionSerializer = new StringSerializer();
    private final BinarySerializer<K> keySerializer = new BinarySerializer<K>();
    private final BinarySerializer<V> valueSerializer = new BinarySerializer<V>();

    public RedisCacheEntity(String name, JedisPool jedisPool) {
        this.bytePrefix = regionSerializer.serialize(name + DOT);
        this.byteCount = regionSerializer.serialize(name + DOT + "_count_");
        this.jedisPool = jedisPool;
    }

    public RedisCacheEntity(String name, JedisPool jedisPool, int size) {
        this(name, jedisPool);
        this.size = size;
    }

    @Override
    public List<V> put(K key, V value) {
        Jedis jedis = jedisPool.getResource();
        byte[] byteKey = getKey(key);
        jedis.set(byteKey, valueSerializer.serialize(value));
        jedis.zadd(bytePrefix, System.currentTimeMillis(), byteKey);
        return clearCache(jedis);
    }

    @Override
    public void put(K key, V value, Integer expiry) {
        Jedis jedis = jedisPool.getResource();
        byte[] byteKey = getKey(key);
        if (null == expiry) {
            jedis.set(byteKey, valueSerializer.serialize(value));
        } else {
            jedis.setex(byteKey, expiry, valueSerializer.serialize(value));
        }
        jedis.zadd(bytePrefix, System.currentTimeMillis(), byteKey);
        jedis.incr(byteCount);
        jedis.close();
    }

    @Override
    public V get(K key) {
        Jedis jedis = jedisPool.getResource();
        byte[] fullKey = getKey(key);
        V value = valueSerializer.deserialize(jedis.get(fullKey));
        jedis.zadd(bytePrefix, System.currentTimeMillis(), fullKey);
        jedis.close();
        return value;
    }

    @Override
    public List<V> clear() {
        Jedis jedis = jedisPool.getResource();
        Set<byte[]> keyList = jedis.zrange(bytePrefix, 0, -1);
        List<V> list = new ArrayList<V>();
        for (byte[] key : keyList) {
            list.add(valueSerializer.deserialize(key));
            jedis.del(key);
        }
        jedis.del(bytePrefix);
        jedis.close();
        return list;
    }

    @Override
    public V remove(K key) {
        Jedis jedis = jedisPool.getResource();
        byte[] byteKey = getKey(key);
        if (jedis.exists(byteKey)) {
            V value = valueSerializer.deserialize(jedis.get(byteKey));
            jedis.del(byteKey);
            jedis.zrem(bytePrefix, byteKey);
            jedis.close();
            return value;
        }
        return null;
    }

    @Override
    public long getDataSize() {
        Jedis jedis = jedisPool.getResource();
        long size = jedis.zrevrange(bytePrefix, 0, -1).size();
        jedis.close();
        return size;
    }

    private List<V> clearCache(Jedis jedis) {
        List<V> list = null;
        if (size < jedis.incr(byteCount)) {
            int helf = size / 2;
            jedis.decrBy(byteCount, helf);
            Set<byte[]> keys = jedis.zrange(bytePrefix, 0, helf);
            list = new ArrayList<V>();
            for (byte[] key : keys) {
                list.add(valueSerializer.deserialize(jedis.get(key)));
                jedis.zrem(bytePrefix, key);
                jedis.del(key);
            }
        }
        jedis.close();
        return list;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public Map<K, V> getAll() {
        Jedis jedis = jedisPool.getResource();
        Set<byte[]> keySet = jedis.zrange(bytePrefix, 0, -1);
        Map<K, V> map = new HashMap<K, V>();
        for (byte[] key : keySet) {
            map.put(keySerializer.deserialize(key), valueSerializer.deserialize(jedis.get(key)));
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
        return addAll(bytePrefix, keySerializer.serialize(key));
    }
}
