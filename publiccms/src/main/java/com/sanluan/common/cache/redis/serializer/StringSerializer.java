package com.sanluan.common.cache.redis.serializer;

import com.sanluan.common.base.Base;

public class StringSerializer extends Base implements Serializer<String> {
    @Override
    public byte[] serialize(String str) {
        return null == str ? EMPTY_BYTES : str.getBytes(DEFAULT_CHARSET);
    }

    @Override
    public String deserialize(byte[] bytes) {
        return bytes == null ? null : new String(bytes, DEFAULT_CHARSET);
    }
}
