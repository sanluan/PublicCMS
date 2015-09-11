package com.sanluan.common.cache.memcached.strategies;

import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

import com.sanluan.common.cache.memcached.regions.MemcachedNaturalIdRegion;

/**
 * @author KwonNam Son (kwon37xi@gmail.com)
 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy
 */
public class ReadWriteMemcachedNaturalIdRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedNaturalIdRegion>
		implements NaturalIdRegionAccessStrategy {
	public ReadWriteMemcachedNaturalIdRegionAccessStrategy(MemcachedNaturalIdRegion region) {
		super(region);
	}

	@Override
	public NaturalIdRegion getRegion() {
		return region();
	}
}
