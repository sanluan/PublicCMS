package com.sanluan.common.cache.memcached.strategies;

import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;

import com.sanluan.common.cache.memcached.regions.MemcachedCollectionRegion;

public class ReadWriteMemcachedCollectionRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedCollectionRegion> implements
		CollectionRegionAccessStrategy {
	public ReadWriteMemcachedCollectionRegionAccessStrategy(MemcachedCollectionRegion region) {
		super(region);
	}

	@Override
	public CollectionRegion getRegion() {
		return region();
	}
}