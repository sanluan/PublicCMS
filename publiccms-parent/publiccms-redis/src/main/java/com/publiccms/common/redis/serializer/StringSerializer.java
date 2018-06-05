package com.publiccms.common.redis.serializer;

import com.publiccms.common.constants.CommonConstants;

/**
 *
 * StringSerializer
 * 
 */
public class StringSerializer implements Serializer<String> {

    @Override
    public byte[] serialize(String str) {
        return null == str ? EMPTY_BYTES : str.getBytes(CommonConstants.DEFAULT_CHARSET);
    }

    @Override
    public String deserialize(byte[] bytes) {
        return null == bytes ? null : new String(bytes, CommonConstants.DEFAULT_CHARSET);
    }
}
