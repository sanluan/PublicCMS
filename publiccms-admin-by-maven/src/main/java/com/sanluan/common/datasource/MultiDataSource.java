package com.sanluan.common.datasource;

import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * MultiDataSource 多数据源解决方案
 *
 */
public class MultiDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();

    @Override
    protected Object determineCurrentLookupKey() {
        return holder.get();
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        super.setDefaultTargetDataSource(targetDataSources.values().iterator().next());
    }

    /**
     * @param name
     */
    public static void setDataSourceName(String name) {
        holder.set(name);
    }
}