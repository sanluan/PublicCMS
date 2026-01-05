package com.publiccms.common.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.redis.serializer.Serializer;
import com.publiccms.common.redis.serializer.StringSerializer;
import com.publiccms.common.redis.serializer.ValueSerializer;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RedisUtils;

import redis.clients.jedis.RedisClient;

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
    private RedisClient redisClient;
    private String region;
    private static final StringSerializer stringSerializer = new StringSerializer();
    private final Serializer<V> valueSerializer = new ValueSerializer<>();

    public static final String CACHE_PREFIX = "cms2025::";

    @Override
    public List<V> put(K key, V value) {
        redisClient.set(getKey(key), valueSerializer.serialize(value));
        return null;
    }

    @Override
    public void put(K key, V value, Long expiryInSeconds) {
        if (null == expiryInSeconds) {
            redisClient.set(getKey(key), valueSerializer.serialize(value));
        } else {
            redisClient.setex(getKey(key), expiryInSeconds, valueSerializer.serialize(value));
        }
    }

    @Override
    public V get(K key) {
        return valueSerializer.deserialize(redisClient.get(getKey(key)));
    }

    @Override
    public V remove(K key) {
        byte[] byteKey = getKey(key);
        V value = valueSerializer.deserialize(redisClient.get(byteKey));
        redisClient.del(byteKey);
        return value;
    }

    @Override
    public List<V> clear(boolean recycling) {
        if (recycling) {
            List<V> list = new ArrayList<>();
            Set<String> keyList = redisClient.keys(CommonUtils.joinString(region, Constants.COLON, "*"));
            keyList.forEach(k -> {
                byte[] byteKey = stringSerializer.serialize(k);
                V value = valueSerializer.deserialize(redisClient.get(byteKey));
                if (0 < redisClient.del(k)) {
                    list.add(value);
                }
            });
            redisClient.close();
            return list;
        } else {
            Set<String> keyList = redisClient.keys(CommonUtils.joinString(region, Constants.COLON, "*"));
            keyList.forEach(k -> {
                redisClient.del(k);
            });
            return null;
        }
    }

    @Override
    public boolean contains(K key) {
        return redisClient.exists(getKey(key));
    }

    private byte[] getKey(K key) {
        return stringSerializer.serialize(CommonUtils.joinString(CACHE_PREFIX, region, Constants.COLON, key));
    }

    @Override
    public RedisCacheEntity<K, V> init(String region, Properties properties) {
        return init(region, RedisUtils.createOrGetJedisPool(properties));
    }

    public RedisCacheEntity<K, V> init(String region, RedisClient redisClient) {
        this.region = region;
        this.redisClient = redisClient;
        return this;
    }

    public String getRegion() {
        return region;
    }

}