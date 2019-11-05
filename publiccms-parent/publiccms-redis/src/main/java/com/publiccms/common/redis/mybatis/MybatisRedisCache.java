package com.publiccms.common.redis.mybatis;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;

import com.publiccms.common.redis.RedisClient;

/**
 *
 * MybatisRedisCache
 * 
 */
public class MybatisRedisCache implements Cache {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    protected RedisClient redisClient;

    private String id;

    /**
     * @param id
     */
    public MybatisRedisCache(String id) {
        if (null == id) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        redisClient.set(id, key, value);
    }

    @Override
    public Object getObject(Object key) {
        return redisClient.get(id, key);
    }

    @Override
    public Object removeObject(Object key) {
        return redisClient.del(id, key);
    }

    @Override
    public void clear() {
        redisClient.flushDb();
    }

    @Override
    public int getSize() {
        return Long.valueOf(redisClient.dbSize()).intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

}
