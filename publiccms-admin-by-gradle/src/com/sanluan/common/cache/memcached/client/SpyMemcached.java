package com.sanluan.common.cache.memcached.client;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import com.sanluan.common.cache.memcached.CacheElement;
import com.sanluan.common.cache.memcached.Memcached;

public class SpyMemcached implements Memcached {
	private MemcachedClient client;

	public void connect(String servers) throws IOException {
		client = new MemcachedClient(AddrUtil.getAddresses(servers));
	}

	@Override
	public void shutdown() {
		client.shutdown();
	}

	@Override
	public CacheElement get(String key) {
		return (CacheElement) client.get(key);
	}

	@Override
	public void set(String key, CacheElement value, int expirySeconds) {
		client.set(key, expirySeconds, value);
	}

	@Override
	public void delete(String key) {
		client.delete(key);
	}

	@Override
	public long incr(String key, long by, long defaultValue, int expirySeconds) {
		return client.incr(key, by, defaultValue, expirySeconds);
	}
}