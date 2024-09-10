package com.publiccms.common.redis.serializer;

import java.nio.charset.StandardCharsets;

/**
 *
 * StringSerializer
 * 
 */
public class StringSerializer implements Serializer<String> {

    @Override
    public byte[] serialize(String str) {
        return null == str ? EMPTY_BYTES : str.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String deserialize(byte[] bytes) {
        return null == bytes ? null : new String(bytes, StandardCharsets.UTF_8);
    }
}
