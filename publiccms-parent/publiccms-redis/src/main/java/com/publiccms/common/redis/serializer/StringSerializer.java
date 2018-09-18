package com.publiccms.common.redis.serializer;

import com.publiccms.common.constants.Constants;

/**
 *
 * StringSerializer
 * 
 */
public class StringSerializer implements Serializer<String> {

    @Override
    public byte[] serialize(String str) {
        return null == str ? EMPTY_BYTES : str.getBytes(Constants.DEFAULT_CHARSET);
    }

    @Override
    public String deserialize(byte[] bytes) {
        return null == bytes ? null : new String(bytes, Constants.DEFAULT_CHARSET);
    }
}
