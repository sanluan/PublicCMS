package com.sanluan.common.cache.memcached;

import java.io.IOException;

public interface Memcached {
	void connect(String servers) throws IOException;

	CacheElement get(String key);

	void set(String key, CacheElement value, int expirySeconds);

	void delete(String key);

	long incr(String key, long by, long defaultValue, int expirySeconds);

	void shutdown();
}