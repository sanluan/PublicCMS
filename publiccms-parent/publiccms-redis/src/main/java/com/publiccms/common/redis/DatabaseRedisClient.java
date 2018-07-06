package com.publiccms.common.redis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.publiccms.common.tools.CommonUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * RedisClient
 */
public class DatabaseRedisClient {
    /**
     * 
     */
    public static final int DEFAULT_EXPIRY_IN_SECONDS = 120;

    protected final Log log = LogFactory.getLog(getClass());
    private static JedisPool jedisPool;
    private Map<String, RedisCacheEntity<Object, Object>> regionMap = new HashMap<>();

    /**
     * @param jedisPool
     */
    public DatabaseRedisClient(JedisPool jedisPool) {
        DatabaseRedisClient.jedisPool = jedisPool;
    }

    /**
     * @return
     */
    public long dbSize() {
        Jedis jedis = jedisPool.getResource();
        long size = jedis.dbSize();
        jedis.close();
        return size;
    }

    /**
     * @param region
     * @return
     */
    public long getDataSize(String region) {
        return getCache(region).getDataSize();
    }

    /**
     * @param region
     * @param key
     * @return
     */
    public boolean exists(String region, Object key) {
        return getCache(region).contains(key.toString());
    }

    /**
     * @param region
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String region, Object key) {
        T cacheItem = (T) getCache(region).get(key.toString());
        log.trace("retrieve cache item. region=" + region + ", key=" + key + ", value=" + cacheItem);
        return cacheItem;
    }

    /**
     * @param region
     * @param key
     * @param value
     */
    public void set(String region, Object key, Object value) {
        set(region, key, value, null);
    }

    /**
     * @param region
     * @param key
     * @param value
     * @param expiry
     */
    public void set(String region, Object key, Object value, Integer expiry) {
        log.trace("set cache item. region=" + region + ", key=" + key + ", timeout=" + (CommonUtils.empty(expiry) ? 0 : expiry));
        getCache(region).put(key.toString(), value, expiry);
    }

    /**
     * @param region
     * @param key
     * @return
     */
    public Object del(String region, Object key) {
        return getCache(region).remove(key.toString());
    }

    /**
     * @param region
     */
    public void deleteRegion(String region) {
        getCache(region).clear();
    }

    /**
     * @param region
     * @return
     */
    public Map<?, ?> getAll(String region) {
        return getCache(region).getAll();
    }

    /**
     * 
     */
    public void flushDb() {
        log.info("flush db...");
        Jedis jedis = jedisPool.getResource();
        jedis.flushDB();
        jedis.close();
    }

    /**
     * @param region
     * @return
     */
    public RedisCacheEntity<Object, Object> getCache(String region) {
        RedisCacheEntity<Object, Object> cache = regionMap.get(region);
        if (null == cache) {
            cache = new RedisCacheEntity<>();
            cache.init(region, null, jedisPool);
            regionMap.put(region, cache);
        }
        return cache;
    }

    /**
     * @return
     */
    public boolean isShutdown() {
        return null != jedisPool && jedisPool.isClosed();
    }

    /**
     * 
     */
    public void shutdown() {
        if (null != jedisPool) {
            jedisPool.destroy();
        }
    }
}
