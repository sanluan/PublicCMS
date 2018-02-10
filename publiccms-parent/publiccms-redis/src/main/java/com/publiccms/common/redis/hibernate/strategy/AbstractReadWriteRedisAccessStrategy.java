package com.publiccms.common.redis.hibernate.strategy;

import java.util.Comparator;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.engine.spi.SessionImplementor;

import com.publiccms.common.redis.hibernate.regions.RedisTransactionalDataRegion;

/**
 *
 * AbstractReadWriteRedisAccessStrategy
 * @param <T> 
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

    public boolean putFromLoad(SessionImplementor session, Object key, Object value, long txTimestamp, Object version,
            boolean minimalPutOverride) {
        region.put(key, value);
        return true;
    }

    /**
     * @return
     */
    public Comparator<T> getVersionComparator() {
        return versionComparator;
    }

}
