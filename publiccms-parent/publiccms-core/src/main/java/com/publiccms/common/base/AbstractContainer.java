package com.publiccms.common.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.api.Container;

public abstract class AbstractContainer<K, T> {
    private Map<K, T> map = new HashMap<>();
    private List<T> list;

    /**
     * @param key
     * @return the value
     */
    public T get(K key) {
        return map.get(key);
    }

    @Autowired(required = false)
    public void init(List<T> list) {
        this.list = list;
        if (null != list) {
            for (T bean : list) {
                @SuppressWarnings("unchecked")
                Container<K> container = (Container<K>) bean;
                map.put(container.keyFunction().get(), bean);
            }
        }
    }

    /**
     * @return the list
     */
    public List<T> getList() {
        return list;
    }
}