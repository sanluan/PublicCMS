package com.sanluan.common.cache.memcached.regions;

import java.util.Properties;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cfg.Settings;

import com.sanluan.common.cache.memcached.Memcached;
import com.sanluan.common.cache.memcached.strategies.NonStrictReadWriteMemcachedEntityRegionAccessStrategy;
import com.sanluan.common.cache.memcached.strategies.ReadOnlyMemcachedEntityRegionAccessStrategy;
import com.sanluan.common.cache.memcached.strategies.ReadWriteMemcachedEntityRegionAccessStrategy;

public class MemcachedEntityRegion extends MemcachedTransactionalDataRegion implements EntityRegion {
	public MemcachedEntityRegion(String regionName, Properties properties, CacheDataDescription metadata, Settings settings,
			Memcached memcached) {
		super(regionName, properties, metadata, settings, memcached);
	}

	@Override
	public EntityRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
		switch (accessType) {
		case READ_ONLY:
			if (getCacheDataDescription().isMutable()) {
				log.info("read-only cache configured for mutable entity {}", getName());
			}
			return new ReadOnlyMemcachedEntityRegionAccessStrategy(this);
		case READ_WRITE:
			return new ReadWriteMemcachedEntityRegionAccessStrategy(this);
		case NONSTRICT_READ_WRITE:
			return new NonStrictReadWriteMemcachedEntityRegionAccessStrategy(this);
		default:
			throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");

		}
	}
}
