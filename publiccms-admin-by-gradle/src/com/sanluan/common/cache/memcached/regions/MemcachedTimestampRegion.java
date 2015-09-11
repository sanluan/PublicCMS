package com.sanluan.common.cache.memcached.regions;

import java.util.Properties;

import org.hibernate.cache.spi.TimestampsRegion;
import org.hibernate.cfg.Settings;

import com.sanluan.common.cache.memcached.Memcached;

public class MemcachedTimestampRegion extends MemcachedGeneralDataRegion implements TimestampsRegion {
	public MemcachedTimestampRegion(String regionName, Properties properties, Settings settings, Memcached memcached) {
		super(regionName, properties, settings, memcached);
	}
}