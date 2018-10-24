package com.publiccms.common.j2cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import com.publiccms.common.cache.CacheEntity;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import net.oschina.j2cache.J2CacheBuilder;
import net.oschina.j2cache.J2CacheConfig;

public class J2CacheEntity<K, V> implements CacheEntity<K, V>, java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CacheChannel channel;
    private String region;

    @Override
    public void init(String region, Properties properties) {
        this.region = region;
        J2CacheConfig config = new J2CacheConfig();
        config.setSerialization(properties.getProperty("j2cache.serialization"));
        config.setBroadcast(properties.getProperty("j2cache.broadcast"));
        config.setL1CacheName(properties.getProperty("j2cache.L1.provider_class"));
        config.setL2CacheName(properties.getProperty("j2cache.L2.provider_class"));
        config.setSyncTtlToRedis(!"false".equalsIgnoreCase(properties.getProperty("j2cache.sync_ttl_to_redis")));
        config.setDefaultCacheNullObject("true".equalsIgnoreCase(properties.getProperty("j2cache.default_cache_null_object")));

        String l2_config_section = properties.getProperty("j2cache.L2.config_section");
        if (l2_config_section == null || l2_config_section.trim().equals("")) {
            l2_config_section = config.getL2CacheName();
        }
        final String l2_section = l2_config_section;
        properties.forEach((k, v) -> {
            String key = (String) k;
            if (key.startsWith(config.getBroadcast() + ".")) {
                config.getBroadcastProperties().setProperty(key.substring((config.getBroadcast() + ".").length()), (String) v);
            }
            if (key.startsWith(config.getL1CacheName() + ".")) {
                config.getL1CacheProperties().setProperty(key.substring((config.getL1CacheName() + ".").length()), (String) v);
            }
            if (key.startsWith(l2_section + ".")) {
                config.getL2CacheProperties().setProperty(key.substring((l2_section + ".").length()), (String) v);
            }

        });
        J2CacheBuilder builder = J2CacheBuilder.init(config);
        channel = builder.getChannel();
    }

    @Override
    public void put(K key, V value, Integer expiryInSeconds) {
        if (null != expiryInSeconds) {
            channel.set(region, key.toString(), value, expiryInSeconds);
        } else {
            channel.set(region, key.toString(), value);
        }
    }

    @Override
    public List<V> put(K key, V value) {
        channel.set(region, key.toString(), value);
        return null;
    }

    @Override
    public V remove(K key) {
        @SuppressWarnings("unchecked")
        V value = (V) channel.get(region, key.toString(), false).getValue();
        channel.evict(region, key.toString());
        return value;
    }

    @Override
    public boolean contains(K key) {
        return channel.exists(region, key.toString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(K key) {
        return (V) channel.get(region, key.toString(), false).getValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<V> clear() {
        synchronized (this) {
            Collection<String> keys = channel.keys(region);
            Map<String, CacheObject> valueMap = channel.get(region, keys);
            channel.clear(region);
            return (List<V>) valueMap.values().stream().map(v -> v.getValue()).collect(Collectors.toList());
        }
    }

}
