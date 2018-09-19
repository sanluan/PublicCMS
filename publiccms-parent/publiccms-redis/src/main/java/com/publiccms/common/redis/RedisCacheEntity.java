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
        return null;
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
        List<V> list = new ArrayList<>();
        Jedis jedis = jedisPool.getResource();
        Set<byte[]> keyList = jedis.zrange(byteRegion, 0, -1);
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
    public void init(String region,  Properties properties) {
        if (null == JEDISPOOL) {
            synchronized (this) {
                if (null == JEDISPOOL) {
                    JEDISPOOL = RedisUtils.createJedisPool(properties);
                }
            }
        }
        init(region, JEDISPOOL);
    }

    public void init(String region, JedisPool pool) {
        this.region = region;
        this.byteRegion = stringSerializer.serialize(region);
        this.jedisPool = pool;
    }

    public String getRegion() {
        return region;
    }

}