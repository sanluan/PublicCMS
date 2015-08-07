package com.sanluan.common.cache.memcached.strategies;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;

import com.sanluan.common.cache.memcached.regions.MemcachedNaturalIdRegion;

/**
 * @author KwonNam Son (kwon37xi@gmail.com)
 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy
 */
public class ReadOnlyMemcachedNaturalIdRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedNaturalIdRegion>
		implements NaturalIdRegionAccessStrategy {
	public ReadOnlyMemcachedNaturalIdRegionAccessStrategy(MemcachedNaturalIdRegion region) {
		super(region);
	}

	@Override
	public NaturalIdRegion getRegion() {
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
