package com.publiccms.common.redis.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.boot.registry.selector.SimpleStrategyRegistrationImpl;
import org.hibernate.boot.registry.selector.StrategyRegistration;
import org.hibernate.boot.registry.selector.StrategyRegistrationProvider;
import org.hibernate.cache.spi.RegionFactory;

public class RedisStrategyRegistrationProviderImpl implements StrategyRegistrationProvider {
    @Override
    @SuppressWarnings({ "rawtypes" })
    public Iterable<StrategyRegistration> getStrategyRegistrations() {
        final List<StrategyRegistration> strategyRegistrations = new ArrayList<>();

        strategyRegistrations.add(new SimpleStrategyRegistrationImpl<RegionFactory>(RegionFactory.class,
                RedisRegionFactory.class, "redis", RedisRegionFactory.class.getName(),
                RedisRegionFactory.class.getSimpleName(), "com.publiccms.common.redis.hibernate.RedisRegionFactory"));

        strategyRegistrations.add(new SimpleStrategyRegistrationImpl<RegionFactory>(RegionFactory.class,
                SingletonRedisRegionFactory.class, "redis-singleton", SingletonRedisRegionFactory.class.getName(),
                SingletonRedisRegionFactory.class.getSimpleName(),
                "com.publiccms.common.redis.hibernate.RedisRegionFactory"));

        return strategyRegistrations;
    }
}
