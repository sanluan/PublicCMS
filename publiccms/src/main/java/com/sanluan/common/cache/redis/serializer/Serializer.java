package com.sanluan.common.cache.redis.serializer;

public interface Serializer<T> {
    public byte[] EMPTY_BYTES = new byte[0];

    public byte[] serialize(final T graph);

    public T deserialize(final byte[] bytes);
}