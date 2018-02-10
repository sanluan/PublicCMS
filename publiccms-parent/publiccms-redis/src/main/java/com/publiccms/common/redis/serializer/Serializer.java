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
    public byte[] EMPTY_BYTES = new byte[0];

    /**
     * @param graph
     * @return
     */
    public byte[] serialize(final T graph);

    /**
     * @param bytes
     * @return
     */
    public T deserialize(final byte[] bytes);
}