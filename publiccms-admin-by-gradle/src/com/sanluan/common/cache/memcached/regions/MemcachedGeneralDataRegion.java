package com.sanluan.common.cache.memcached.regions;

import java.util.Properties;

import org.hibernate.cache.spi.GeneralDataRegion;
import org.hibernate.cfg.Settings;

import com.sanluan.common.cache.memcached.Memcached;

public class MemcachedGeneralDataRegion extends MemcachedDataRegion implements GeneralDataRegion {

	public MemcachedGeneralDataRegion(String regionName, Properties properties, 
			Settings settings, Memcached memcached) {
		super(regionName, properties,  settings, memcached);
	}

}
