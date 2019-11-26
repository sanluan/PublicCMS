package com.publiccms.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * <p>
 * 多数据源解决方案
 * </p>
 * 
 * <p>
 * MultiDataSource
 * </p>
 *
 */
public class MultiDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return HOLDER.get();
    }

    /**
     * @return name
     *         <p>
     *         数据源名称
     *         </p>
     *         <p>
     *         datasource name
     *         </p>
     */
    public static String getDataSourceName() {
        return HOLDER.get();
    }

    /**
     * @param name
     *            <p>
     *            数据源名称
     *            </p>
     *            <p>
     *            datasource name
     *            </p>
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