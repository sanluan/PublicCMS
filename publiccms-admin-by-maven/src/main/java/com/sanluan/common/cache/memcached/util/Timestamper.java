package com.sanluan.common.cache.memcached.util;


public class Timestamper {
	public static long next() {
		return System.currentTimeMillis();
	}
}
