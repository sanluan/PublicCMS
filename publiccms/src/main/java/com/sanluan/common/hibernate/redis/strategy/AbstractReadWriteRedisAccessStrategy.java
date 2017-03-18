package com.sanluan.common.hibernate.redis.strategy;

import java.util.Comparator;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.engine.spi.SessionImplementor;

import com.sanluan.common.hibernate.redis.regions.RedisTransactionalDataRegion;

/**
 *
 * AbstractReadWriteRedisAccessStrategy
 * 
 */
public abstract class AbstractReadWriteRedisAccessStrategy<T extends RedisTransactionalDataRegion>
        extends AbstractRedisAccessStrategy<T> {

    private final Comparator<T> versionComparator;

    /**
     * @param region
     * @param options
     */
    @SuppressWarnings("unchecked")
    public AbstractReadWriteRedisAccessStrategy(T region, SessionFactoryOptions options) {
        super(region, options);
        this.versionComparator = region.getCacheDataDescription().getVersionComparator();
    }

    /* (non-Javadoc)
     * @see com.sanluan.common.hibernate.redis.strategy.AbstractRedisAccessStrategy#putFromLoad(org.hibernate.engine.spi.SessionImplementor, java.lang.Object, java.lang.Object, long, java.lang.Object, boolean)
     */
    public boolean putFromLoad(SessionImplementor session, Object key, Object value, long txTimestamp, Object version,
            boolean minimalPutOverride) {
        region.put(key, value);
        return true;
    }

    public Comparator<T> getVersionComparator() {
        return versionComparator;
    }

}
