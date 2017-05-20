package com.publiccms.common.datasource;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * MultiDataSource 多数据源解决方案
 *
 */
public class MultiDataSource extends AbstractRoutingDataSource {
    
    private static final ThreadLocal<String> HOLDER = new ThreadLocal<String>();

    @Override
    protected Object determineCurrentLookupKey() {
        return HOLDER.get();
    }

    /**
     * @param targetDataSources
     * @param defaultDatabase
     */
    public void setTargetDataSources(Map<Object, Object> targetDataSources, DataSource defaultDatabase) {
        setTargetDataSources(targetDataSources);
        if (null != defaultDatabase) {
            setDefaultTargetDataSource(defaultDatabase);
        }
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