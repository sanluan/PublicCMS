package com.sanluan.common.cache.memcached.regions;

import java.util.Properties;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cfg.Settings;

import com.sanluan.common.cache.memcached.Memcached;
import com.sanluan.common.cache.memcached.strategies.NonStrictReadWriteMemcachedCollectionRegionAccessStrategy;
import com.sanluan.common.cache.memcached.strategies.ReadOnlyMemcachedCollectionRegionAccessStrategy;
import com.sanluan.common.cache.memcached.strategies.ReadWriteMemcachedCollectionRegionAccessStrategy;

public class MemcachedCollectionRegion extends MemcachedTransactionalDataRegion implements CollectionRegion {
	public MemcachedCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata, Settings settings,
			Memcached memcached) {
		super(regionName, properties, metadata, settings, memcached);
	}

	@Override
	public CollectionRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
		switch (accessType) {
		case READ_ONLY:
			if (getCacheDataDescription().isMutable()) {
				log.info("read-only cache configured for mutable entity {}", getName());
			}
			return new ReadOnlyMemcachedCollectionRegionAccessStrategy(this);
		case READ_WRITE:
			return new ReadWriteMemcachedCollectionRegionAccessStrategy(this);
		case NONSTRICT_READ_WRITE:
			return new NonStrictReadWriteMemcachedCollectionRegionAccessStrategy(this);
		default:
			throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");

		}
	}
}
