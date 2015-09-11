package com.sanluan.common.cache.memcached.strategies;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;

import com.sanluan.common.cache.memcached.regions.MemcachedNaturalIdRegion;

/**
 * @author KwonNam Son (kwon37xi@gmail.com)
 */
public class NonStrictReadWriteMemcachedNaturalIdRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedNaturalIdRegion>
		implements NaturalIdRegionAccessStrategy {
	public NonStrictReadWriteMemcachedNaturalIdRegionAccessStrategy(MemcachedNaturalIdRegion region) {
		super(region);
	}

	@Override
	public NaturalIdRegion getRegion() {
		return region();
	}
	
	@Override
	public void unlockItem(Object key, SoftLock lock) throws CacheException {
		remove(key);
	}
}
