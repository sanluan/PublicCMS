package com.sanluan.common.cache.memcached.strategies;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sanluan.common.cache.memcached.regions.MemcachedTransactionalDataRegion;

/**
 * Basic Memcached Region Access Strategy.
 * <p/>
 * This strategy is for READ_ONLY and NONSTRICT_READ_WRITE. This is not suitable
 * for READ_WRITE and TRANSACTIONAL. READ_WRITE, TRANSACTION strategy must
 * override this class's methods.
 *
 * @author KwonNam Son (kwon37xi@gmail.com)
 */
public class AbstractMemcachedAccessStrategy<T extends MemcachedTransactionalDataRegion> {
	protected Logger log = LoggerFactory.getLogger(getClass());

	private final T region;

	public AbstractMemcachedAccessStrategy(T region) {
		this.region = region;
	}

	protected T region() {
		return region;
	}

	public boolean insert(Object key, Object value) throws CacheException {
		return false;
	}

	public boolean afterInsert(Object key, Object value) throws CacheException {
		return false;
	}

	public boolean update(Object key, Object value) throws CacheException {
		remove(key);
		return false;
	}

	public boolean afterUpdate(Object key, Object value, SoftLock lock) throws CacheException {
		unlockItem(key, lock);
		return false;
	}

	public boolean insert(Object key, Object value, Object version) throws CacheException {
		return insert(key, value);
	}

	public boolean afterInsert(Object key, Object value, Object version) throws CacheException {
		return afterInsert(key, value);
	}

	public boolean update(Object key, Object value, Object currentVersion, Object previousVersion) throws CacheException {
		return update(key, value);
	}

	public boolean afterUpdate(Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock)
			throws CacheException {
		return afterUpdate(key, value, lock);
	}

	public Object get(Object key, long txTimestamp) throws CacheException {
		return region.get(key);
	}

	public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version) throws CacheException {
		return putFromLoad(key, value, txTimestamp, version, isMinimalPutsEnabled());
	}

	public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride)
			throws CacheException {
		if (minimalPutOverride && null != region.get(key)) {
			return false;
		}
		region.put(key, value);
		return true;
	}

	public SoftLock lockItem(Object key, Object version) throws CacheException {
		return null;
	}

	public SoftLock lockRegion() throws CacheException {
		return null;
	}

	public void unlockItem(Object key, SoftLock lock) throws CacheException {
	}

	public void unlockRegion(SoftLock lock) throws CacheException {
		evictAll();
	}

	public void remove(Object key) throws CacheException {
		evict(key);
	}

	public void removeAll() throws CacheException {
		evictAll();
	}

	public void evict(Object key) throws CacheException {
		log.debug("{} MemcachedAccessStrategy.evict({})", region.getName(), key);
		region.evict(key);
	}

	public void evictAll() throws CacheException {
		log.debug("{} MemcachedAccessStrategy.evictAll()", region.getName());
		region.evictAll();
	}

	public boolean isMinimalPutsEnabled() {
		return region.getSettings().isMinimalPutsEnabled();
	}
}