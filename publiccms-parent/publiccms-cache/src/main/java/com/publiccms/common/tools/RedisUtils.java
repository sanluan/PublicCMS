package com.publiccms.common.tools;

import java.util.Properties;

import redis.clients.jedis.ConnectionPoolConfig;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.RedisClient;

/**
 *
 * RedisUtils
 * 
 */
public class RedisUtils {
    private static volatile RedisClient redisClient;

    /**
     * @param redisProperties
     * @return
     */
    public static RedisClient createJedisPool(Properties redisProperties) {

        String host = redisProperties.getProperty("redis.host", "localhost");
        int port = Integer.parseInt(redisProperties.getProperty("redis.port", "6379"));
        int timeout = Integer.parseInt(redisProperties.getProperty("redis.timeout", "3000"));
        int maxidle = Integer.parseInt(redisProperties.getProperty("redis.maxidle", "10"));
        String password = redisProperties.getProperty("redis.password");
        String user = redisProperties.getProperty("redis.user");
        String databaseValue = redisProperties.getProperty("redis.database");
        int database = 0;
        if (CommonUtils.notEmpty(database)) {
            database = Integer.parseInt(databaseValue);
        }

        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        poolConfig.setMaxIdle(maxidle);
        return RedisClient.builder().hostAndPort(host, port)
                .clientConfig(DefaultJedisClientConfig.builder().socketTimeoutMillis(timeout).connectionTimeoutMillis(timeout)
                        .user(CommonUtils.empty(user) ? null : user).password(CommonUtils.empty(password) ? null : password)
                        .database(database).build())
                .poolConfig(poolConfig).build();
    }

    /**
     * @param redisProperties
     * @return
     */
    public static RedisClient createOrGetJedisPool(Properties redisProperties) {
        if (null == redisClient) {
            synchronized (RedisUtils.class) {
                if (null == redisClient) {
                    redisClient = createJedisPool(redisProperties);
                }
            }
        }
        return redisClient;
    }
}
