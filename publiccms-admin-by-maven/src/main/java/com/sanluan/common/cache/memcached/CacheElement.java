package com.sanluan.common.cache.memcached;

import java.io.Serializable;

import net.sf.ehcache.Element;

public class CacheElement implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String key;
	private final Object value;

	public CacheElement(final String key, final Object value) {
		this.key = key;
		this.value = value;
	}

	public final String getObjectKey() {
		return key;
	}

	public final Object getObjectValue() {
		return value;
	}

	@Override
	public final boolean equals(final Object object) {
		if (null == object || !(object instanceof Element)) {
			return false;
		}
		CacheElement element = (CacheElement) object;
		if (null == key || null == element.getObjectKey()) {
			return false;
		}
		return key.equals(element.getObjectKey());
	}

	@Override
	public final int hashCode() {
		return key.hashCode();
	}

	@Override
	public final String toString() {
		return new StringBuilder().append("[ key = ").append(key).append(", value=").append(value).append(" ]").toString();
	}
}
