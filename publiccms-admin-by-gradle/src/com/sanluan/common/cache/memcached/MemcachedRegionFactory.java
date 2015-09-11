package com.sanluan.common.cache.memcached;

import java.io.IOException;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.TimestampsRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sanluan.common.cache.memcached.client.SpyMemcached;
import com.sanluan.common.cache.memcached.regions.MemcachedCollectionRegion;
import com.sanluan.common.cache.memcached.regions.MemcachedEntityRegion;
import com.sanluan.common.cache.memcached.regions.MemcachedNaturalIdRegion;
import com.sanluan.common.cache.memcached.regions.MemcachedQueryResultsRegion;
import com.sanluan.common.cache.memcached.regions.MemcachedTimestampRegion;
import com.sanluan.common.cache.memcached.util.Timestamper;

public class MemcachedRegionFactory implements RegionFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String PROP_PREFIX = "hibernate.memcached.";
	public static final String PROP_SERVERS = PROP_PREFIX + "servers";
	public static final String DEFAULT_SERVER = "localhost:11211";
	public static final int MAX_EPIRY = 60 * 60 * 24 * 30;
	public static final String PROP_CLIENT = PROP_PREFIX + "client";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private Settings settings;
	private Memcached memcached;

	@Override
	public void start(Settings settings, Properties properties) throws CacheException {
		log.debug("MemcachedRegionFactory.start()");
		this.settings = settings;
		String memcachedClient = properties.getProperty(PROP_CLIENT);
		if (isNotBlank(memcachedClient)) {
			try {
				@SuppressWarnings("unchecked")
				Class<Memcached> clazz = (Class<Memcached>) Class.forName(memcachedClient);
				memcached = clazz.newInstance();
			} catch (Exception e) {
			}
		}
		if (null == memcached)
			memcached = new SpyMemcached();
		try {
			memcached.connect(properties.getProperty(PROP_SERVERS, DEFAULT_SERVER));
		} catch (IOException e) {
			throw new CacheException("Unable to connect to Memcached", e);
		}
	}

	@Override
	public void stop() {
		log.debug("MemcachedRegionFactory.stop()");
		memcached.shutdown();
	}

	@Override
	public boolean isMinimalPutsEnabledByDefault() {
		return false;
	}

	@Override
	public AccessType getDefaultAccessType() {
		return AccessType.READ_WRITE;
	}

	@Override
	public long nextTimestamp() {
		return Timestamper.next();
	}

	@Override
	public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata)
			throws CacheException {
		return new MemcachedEntityRegion(regionName, properties, metadata, settings, memcached);
	}

	@Override
	public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata)
			throws CacheException {
		return new MemcachedNaturalIdRegion(regionName, properties, metadata, settings, memcached);
	}

	@Override
	public CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata)
			throws CacheException {
		return new MemcachedCollectionRegion(regionName, properties, metadata, settings, memcached);
	}

	@Override
	public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
		return new MemcachedQueryResultsRegion(regionName, properties, settings, memcached);
	}

	@Override
	public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
		return new MemcachedTimestampRegion(regionName, properties, settings, memcached);
	}
}