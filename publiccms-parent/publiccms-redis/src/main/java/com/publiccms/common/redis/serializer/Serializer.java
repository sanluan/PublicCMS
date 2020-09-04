package com.publiccms.common.redis.serializer;

/**
 *
 * Serializer
 * @param <T> 
 * 
 */
public interface Serializer<T> {
    
    /**
     * 
     */
    byte[] EMPTY_BYTES = new byte[0];

    /**
     * @param graph
     * @return
     */
    byte[] serialize(final T graph);

    /**
     * @param bytes
     * @return
     */
    T deserialize(final byte[] bytes);
}