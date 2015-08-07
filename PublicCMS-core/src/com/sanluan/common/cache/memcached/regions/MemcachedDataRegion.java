package com.sanluan.common.cache.memcached.regions;

import static com.sanluan.common.cache.memcached.MemcachedRegionFactory.MAX_EPIRY;
import static com.sanluan.common.cache.memcached.MemcachedRegionFactory.PROP_PREFIX;

import java.util.Map;
import java.util.Properties;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.Region;
import org.hibernate.cfg.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sanluan.common.cache.memcached.CacheElement;
import com.sanluan.common.cache.memcached.Memcached;
import com.sanluan.common.cache.memcached.util.Timestamper;

public class MemcachedDataRegion implements Region {
	protected Logger log = LoggerFactory.getLogger(getClass());

	private static final int UNKNOWN = -1;
	private static final int DEFAULT_CACHE_LOCK_TIMEOUT_MILLIS = 60 * 1000;
	private static final String VERSION = "_version";
	private static final String PROP_REGION_EXPIRY = PROP_PREFIX + "expiry";
	private static final String DEFAULT_EPIRY_SECONDS = "1800";

	private String regionName;
	private Properties properties;
	private Settings settings;
	private Memcached memcached;
	private int expiry;

	String getKey(Object key) {
		long version = getCache().incr(regionName + VERSION, 0, System.currentTimeMillis(), MAX_EPIRY);
		return regionName + version + ":" + key;
	}

	public MemcachedDataRegion(String regionName, Properties properties, Settings settings, Memcached memcached) {
		this.regionName = regionName;
		this.properties = properties;
		this.settings = settings;
		this.memcached = memcached;
		String expiryProperty = properties.getProperty(PROP_REGION_EXPIRY + "." + regionName,
				properties.getProperty(PROP_REGION_EXPIRY, DEFAULT_EPIRY_SECONDS));
		expiry = Integer.parseInt(expiryProperty);
		log.debug("new Region({}) expiry time : {}", regionName, expiry);
	}

	public Properties getProperties() {
		return properties;
	}

	public Settings getSettings() {
		return settings;
	}

	public Memcached getCache() {
		return memcached;
	}

	@Override
	public String getName() {
		return regionName;
	}

	@Override
	public void destroy() throws CacheException {
	}

	public Object get(Object key) throws CacheException {
		String refinedKey = getKey(key);
		try {
			final CacheElement element = getCache().get(refinedKey);
			if (null == element) {
				return null;
			} else {
				return element.getObjectValue();
			}
		} catch (Exception e) {
			return null;
		}
	}

	public void put(Object key, Object value) throws CacheException {
		String refinedKey = getKey(key);
		getCache().set(refinedKey, new CacheElement(refinedKey, value), getExpiryInSeconds());
	}

	public void evict(Object key) throws CacheException {
		String refinedKey = getKey(key);
		log.debug("{} Region.evict({})", regionName, refinedKey);
		getCache().delete(refinedKey);
	}

	public void evictAll() throws CacheException {
		log.debug("{} Region.evictAll()", regionName);
		getCache().incr(regionName + VERSION, 1, System.currentTimeMillis(), MAX_EPIRY);
	}

	@Override
	public boolean contains(Object key) {
		return false;
	}

	@Override
	public long getSizeInMemory() {
		return UNKNOWN;
	}

	@Override
	public long getElementCountInMemory() {
		return UNKNOWN;
	}

	@Override
	public long getElementCountOnDisk() {
		return UNKNOWN;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map toMap() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long nextTimestamp() {
		return Timestamper.next();
	}

	@Override
	public int getTimeout() {
		return DEFAULT_CACHE_LOCK_TIMEOUT_MILLIS;
	}

	/**
	 * Read expiry seconds from configuration properties
	 */
	protected int getExpiryInSeconds() {
		return expiry;
	}
}