package com.sanluan.common.cache.memcached.strategies;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;

import com.sanluan.common.cache.memcached.regions.MemcachedCollectionRegion;

public class ReadOnlyMemcachedCollectionRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedCollectionRegion> implements
		CollectionRegionAccessStrategy {
	public ReadOnlyMemcachedCollectionRegionAccessStrategy(MemcachedCollectionRegion region) {
		super(region);
	}

	@Override
	public CollectionRegion getRegion() {
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