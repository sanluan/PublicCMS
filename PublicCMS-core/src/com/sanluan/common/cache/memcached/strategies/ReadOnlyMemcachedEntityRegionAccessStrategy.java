package com.sanluan.common.cache.memcached.strategies;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;

import com.sanluan.common.cache.memcached.regions.MemcachedEntityRegion;

/**
 * ReadOnly concurrency strategy.
 *
 * @author KwonNam Son (kwon37xi@gmail.com)
 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy
 */
public class ReadOnlyMemcachedEntityRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedEntityRegion> implements
		EntityRegionAccessStrategy {
	public ReadOnlyMemcachedEntityRegionAccessStrategy(MemcachedEntityRegion region) {
		super(region);
	}

	@Override
	public EntityRegion getRegion() {
		return region();
	}
	
	@Override
	public SoftLock lockItem(Object key, Object version) throws UnsupportedOperationException {
		return null;
	}

	@Override
	public void unlockItem(Object key, SoftLock lock) throws CacheException {
	}
}
