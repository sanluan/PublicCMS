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
    private JedisPool jedisPool;
    private String region;
    private final static StringSerializer stringSerializer = new StringSerializer();
    private final BinarySerializer<V> valueSerializer = new BinarySerializer<>();

    @Override
    public List<V> put(K key, V value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(getKey(key), valueSerializer.serialize(value));
        jedis.close();
        return null;
    }

    @Override
    public void put(K key, V value, Integer expiryInSeconds) {
        Jedis jedis = jedisPool.getResource();
        if (null == expiryInSeconds) {
            jedis.set(getKey(key), valueSerializer.serialize(value));
        } else {
            jedis.setex(getKey(key), expiryInSeconds, valueSerializer.serialize(value));
        }
        jedis.close();
    }

    @Override
    public V get(K key) {
        Jedis jedis = jedisPool.getResource();
        V value = valueSerializer.deserialize(jedis.get(getKey(key)));
        jedis.close();
        return value;
    }

    @Override
    public V remove(K key) {
        Jedis jedis = jedisPool.getResource();
        byte[] byteKey = getKey(key);
        V value = null;
        if (0 < jedis.del(byteKey)) {
            value = valueSerializer.deserialize(jedis.get(byteKey));
        }
        jedis.close();
        return value;
    }

    @Override
    public List<V> clear() {
        List<V> list = new ArrayList<>();
        Jedis jedis = jedisPool.getResource();
        Set<String> keyList = jedis.keys(region + Constants.DOT + "*");
        for (String key : keyList) {
            if (0 < jedis.del(key)) {
                byte[] byteKey = stringSerializer.serialize(key);
                V value = valueSerializer.deserialize(jedis.get(byteKey));
                list.add(value);
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
    public void init(String region, Properties properties) {
        init(region, RedisUtils.createOrGetJedisPool(properties));
    }

    public void init(String region, JedisPool pool) {
        this.region = region;
        this.jedisPool = pool;
    }

    public String getRegion() {
        return region;
    }

}