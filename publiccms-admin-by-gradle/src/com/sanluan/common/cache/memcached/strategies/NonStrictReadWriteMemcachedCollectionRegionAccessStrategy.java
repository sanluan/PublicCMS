package com.sanluan.common.cache.memcached.strategies;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;

import com.sanluan.common.cache.memcached.regions.MemcachedCollectionRegion;

/**
 * @author KwonNam Son (kwon37xi@gmail.com)
 */
public class NonStrictReadWriteMemcachedCollectionRegionAccessStrategy extends
		AbstractMemcachedAccessStrategy<MemcachedCollectionRegion> implements CollectionRegionAccessStrategy {
	public NonStrictReadWriteMemcachedCollectionRegionAccessStrategy(MemcachedCollectionRegion region) {
		super(region);
	}

	@Override
	public CollectionRegion getRegion() {
		return region();
	}

	@Override
	public void unlockItem(Object key, SoftLock lock) throws CacheException {
		remove(key);
	}
}