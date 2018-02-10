package com.publiccms.common.redis.mybatis;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;

import com.publiccms.common.redis.DatabaseRedisClient;

/**
 *
 * MybatisRedisCache
 * 
 */
public class MybatisRedisCache implements Cache {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    protected DatabaseRedisClient redisClient;

    private String id;

    /**
     * @param id
     */
    public MybatisRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void putObject(Object key, Object value) {
        redisClient.set(id, key, value);
    }

    public Object getObject(Object key) {
        return redisClient.get(id, key);
    }

    public Object removeObject(Object key) {
        return redisClient.del(id, key);
    }

    public void clear() {
        redisClient.flushDb();
    }

    public int getSize() {
        return new Long(redisClient.dbSize()).intValue();
    }

    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

}
