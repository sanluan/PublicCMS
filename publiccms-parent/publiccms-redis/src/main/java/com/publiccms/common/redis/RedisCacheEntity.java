package com.publiccms.common.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.constants.Constants;
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
public class RedisCacheEntity<K, V> implements CacheEntity<K, V>, java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int size = 100;
    private static JedisPool JEDISPOOL;
    private JedisPool jedisPool;
    private String region;
    private byte[] byteRegion;
    private final static StringSerializer stringSerializer = new StringSerializer();
    private final BinarySerializer<K> keySerializer = new BinarySerializer<>();
    private final BinarySerializer<V> valueSerializer = new BinarySerializer<>();

    @Override
    public List<V> put(K key, V value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(getKey(key), valueSerializer.serialize(value));
        jedis.zadd(byteRegion, System.currentTimeMillis(), keySerializer.serialize(key));
        return clearCache(jedis);
    }

    @Override
    public void put(K key, V value, Integer expiryInSeconds) {
        Jedis jedis = jedisPool.getResource();
        if (null == expiryInSeconds) {
            jedis.set(getKey(key), valueSerializer.serialize(value));
            jedis.zadd(byteRegion, System.currentTimeMillis(), keySerializer.serialize(key));
        } else {
            jedis.setex(getKey(key), expiryInSeconds, valueSerializer.serialize(value));
        }
        jedis.close();
    }

    @Override
    public V get(K key) {
        Jedis jedis = jedisPool.getResource();
        V value = valueSerializer.deserialize(jedis.get(getKey(key)));
        jedis.zadd(byteRegion, System.currentTimeMillis(), keySerializer.serialize(key));
        jedis.close();
        return value;
    }

    @Override
    public V remove(K key) {
        Jedis jedis = jedisPool.getResource();
        byte[] byteKey = getKey(key);
        V value = null;
        if (1 == jedis.zrem(byteRegion, keySerializer.serialize(key))) {
            value = valueSerializer.deserialize(jedis.get(byteKey));
            jedis.del(byteKey);
        }
        jedis.close();
        return value;
    }

    @Override
    public List<V> clear() {
        Jedis jedis = jedisPool.getResource();
        Set<byte[]> keyList = jedis.zrange(byteRegion, 0, -1);
        List<V> list = new ArrayList<>();
        if (0 < jedis.del(byteRegion)) {
            for (byte[] byteKey : keyList) {
                byte[] key = getKey(keySerializer.deserialize(byteKey));
                list.add(valueSerializer.deserialize(jedis.get(key)));
                jedis.del(key);
            }
        }
        jedis.close();
        return list;
    }

    private List<V> clearCache(Jedis jedis) {
        List<V> list = null;
        if (size < jedis.zcount(byteRegion, 0, Long.MAX_VALUE)) {
            int helf = size / 2;
            Set<byte[]> keys = jedis.zrange(byteRegion, 0, helf);
            list = new ArrayList<>();
            for (byte[] byteKey : keys) {
                if (1 == jedis.zrem(byteRegion, byteKey)) {
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
    public boolean contains(K key) {
        Jedis jedis = jedisPool.getResource();
        boolean exits = jedis.exists(getKey(key));
        jedis.close();
        return exits;
    }

    private byte[] getKey(K key) {
        return stringSerializer.serialize(region + Constants.DOT + key);
    }

    @Override
    public void init(String region, Integer cacheSize, Properties properties) {
        if (null == JEDISPOOL) {
            synchronized (this) {
                if (null == JEDISPOOL) {
                    JEDISPOOL = RedisUtils.createJedisPool(properties);
                }
            }
        }
        init(region, cacheSize, JEDISPOOL);
    }

    public void init(String region, Integer cacheSize, JedisPool pool) {
        this.region = region;
        this.byteRegion = stringSerializer.serialize(region);
        if (null != cacheSize) {
            this.size = cacheSize;
        }
        this.jedisPool = pool;
    }

    public String getRegion() {
        return region;
    }

}