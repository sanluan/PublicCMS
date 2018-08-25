package com.publiccms.common.cache;

public class CacheValue<V> {
    private Long expiryDate;
    private V value;

    public CacheValue(V value) {
        this.value = value;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
