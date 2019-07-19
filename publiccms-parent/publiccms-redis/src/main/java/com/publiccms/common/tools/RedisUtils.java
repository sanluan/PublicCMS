package com.publiccms.common.tools;

import java.util.Properties;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * RedisUtils
 * 
 */
public class RedisUtils {
    private static JedisPool pool;

    /**
     * @param redisProperties
     * @return
     */
    public static JedisPool createJedisPool(Properties redisProperties) {
        String host = redisProperties.getProperty("redis.host", "localhost");
        int port = Integer.valueOf(redisProperties.getProperty("redis.port", "6379"));
        int timeout = Integer.valueOf(redisProperties.getProperty("redis.timeout", "3000"));
        int maxidle = Integer.valueOf(redisProperties.getProperty("redis.maxidle", "10"));
        String password = redisProperties.getProperty("redis.password");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxidle);
        return new JedisPool(config, host, port, timeout, password);
    }

    /**
     * @param redisProperties
     * @return
     */
    public static JedisPool createOrGetJedisPool(Properties redisProperties) {
        if (null == pool) {
            synchronized (RedisUtils.class) {
                if (null == pool) {
                    pool = createJedisPool(redisProperties);
                }
            }
        }
        return pool;
    }
}
