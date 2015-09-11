package com.sanluan.common.cache.memcached.regions;

import java.util.Properties;

import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.TransactionalDataRegion;
import org.hibernate.cfg.Settings;

import com.sanluan.common.cache.memcached.Memcached;

public class MemcachedTransactionalDataRegion extends MemcachedDataRegion implements TransactionalDataRegion {

	private CacheDataDescription metadata;

	public MemcachedTransactionalDataRegion(String regionName, Properties properties, CacheDataDescription metadata,
			Settings settings, Memcached memcached) {
		super(regionName, properties, settings, memcached);
		this.metadata = metadata;
	}

	@Override
	public boolean isTransactionAware() {
		return false;
	}

	@Override
	public CacheDataDescription getCacheDataDescription() {
		return metadata;
	}
}
