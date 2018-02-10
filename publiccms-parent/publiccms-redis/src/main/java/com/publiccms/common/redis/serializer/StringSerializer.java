package com.publiccms.common.redis.serializer;

import com.publiccms.common.base.Base;
/**
 *
 * StringSerializer
 * 
 */
public class StringSerializer implements Serializer<String>,Base {
    
    @Override
    public byte[] serialize(String str) {
        return null == str ? EMPTY_BYTES : str.getBytes(DEFAULT_CHARSET);
    }

    @Override
    public String deserialize(byte[] bytes) {
        return bytes == null ? null : new String(bytes, DEFAULT_CHARSET);
    }
}
