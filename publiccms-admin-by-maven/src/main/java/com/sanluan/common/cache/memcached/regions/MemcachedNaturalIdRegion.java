package com.sanluan.common.cache.memcached.regions;

import java.util.Properties;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cfg.Settings;

import com.sanluan.common.cache.memcached.Memcached;
import com.sanluan.common.cache.memcached.strategies.NonStrictReadWriteMemcachedNaturalIdRegionAccessStrategy;
import com.sanluan.common.cache.memcached.strategies.ReadOnlyMemcachedNaturalIdRegionAccessStrategy;
import com.sanluan.common.cache.memcached.strategies.ReadWriteMemcachedNaturalIdRegionAccessStrategy;

public class MemcachedNaturalIdRegion extends MemcachedTransactionalDataRegion implements NaturalIdRegion {
	public MemcachedNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata, Settings settings,
			Memcached memcached) {
		super(regionName, properties, metadata, settings, memcached);
	}

	@Override
	public NaturalIdRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
		switch (accessType) {
		case READ_ONLY:
			if (getCacheDataDescription().isMutable()) {
				log.info("read-only cache configured for mutable entity {}", getName());
			}
			return new ReadOnlyMemcachedNaturalIdRegionAccessStrategy(this);
		case READ_WRITE:
			return new ReadWriteMemcachedNaturalIdRegionAccessStrategy(this);
		case NONSTRICT_READ_WRITE:
			return new NonStrictReadWriteMemcachedNaturalIdRegionAccessStrategy(this);
		default:
			throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");

		}
	}
}
