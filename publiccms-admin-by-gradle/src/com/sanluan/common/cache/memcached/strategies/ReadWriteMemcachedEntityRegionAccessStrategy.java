package com.sanluan.common.cache.memcached.strategies;

import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;

import com.sanluan.common.cache.memcached.regions.MemcachedEntityRegion;

/**
 * ReadOnly concurrency strategy.
 *
 * @author KwonNam Son (kwon37xi@gmail.com)
 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy
 */
public class ReadWriteMemcachedEntityRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedEntityRegion> implements
		EntityRegionAccessStrategy {
	public ReadWriteMemcachedEntityRegionAccessStrategy(MemcachedEntityRegion region) {
		super(region);
	}

	@Override
	public EntityRegion getRegion() {
		return region();
	}
}
