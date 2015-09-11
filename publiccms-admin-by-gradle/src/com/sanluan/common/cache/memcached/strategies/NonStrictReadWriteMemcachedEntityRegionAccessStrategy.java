package com.sanluan.common.cache.memcached.strategies;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;

import com.sanluan.common.cache.memcached.regions.MemcachedEntityRegion;

public class NonStrictReadWriteMemcachedEntityRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedEntityRegion>
		implements EntityRegionAccessStrategy {
	public NonStrictReadWriteMemcachedEntityRegionAccessStrategy(MemcachedEntityRegion region) {
		super(region);
	}

	@Override
	public EntityRegion getRegion() {
		return region();
	}
	@Override
	public void unlockItem(Object key, SoftLock lock) throws CacheException {
		remove(key);
	}
}
