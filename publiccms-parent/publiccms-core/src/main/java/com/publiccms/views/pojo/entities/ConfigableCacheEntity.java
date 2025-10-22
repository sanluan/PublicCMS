package com.publiccms.views.pojo.entities;

import java.util.Map;

/**
 * @param <E>
 */
public class ConfigableCacheEntity<E> implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private E entity;
    private Map<String, String> config;

    public ConfigableCacheEntity(E entity, Map<String, String> config) {
        super();
        this.entity = entity;
        this.config = config;
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

}
