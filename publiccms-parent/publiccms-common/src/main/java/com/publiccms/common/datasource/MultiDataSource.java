package com.publiccms.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * MultiDataSource 多数据源解决方案
 *
 */
public class MultiDataSource extends AbstractRoutingDataSource {
    
    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return HOLDER.get();
    }

    /**
     * @param name
     */
    public static void setDataSourceName(String name) {
        HOLDER.set(name);
    }

    /**
     * 
     */
    public static void resetDataSourceName() {
        HOLDER.remove();
    }
}