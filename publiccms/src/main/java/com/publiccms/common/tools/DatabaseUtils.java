package com.publiccms.common.tools;

import static java.lang.Integer.parseInt;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DatabaseUtils {
    public static ComboPooledDataSource getDataSource(String configFile) throws IOException, PropertyVetoException {
        Properties dbconfigProperties = loadAllProperties(configFile);
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(dbconfigProperties.getProperty("jdbc.driverClassName"));
        dataSource.setJdbcUrl(dbconfigProperties.getProperty("jdbc.url"));
        dataSource.setUser(dbconfigProperties.getProperty("jdbc.username"));
        dataSource.setPassword(dbconfigProperties.getProperty("jdbc.password"));
        dataSource.setAutoCommitOnClose(true);
        dataSource.setCheckoutTimeout(5000);
        dataSource.setAcquireRetryAttempts(2);
        dataSource.setInitialPoolSize(1);
        dataSource.setMinPoolSize(1);
        dataSource.setMaxPoolSize(3);
        dataSource.setMaxIdleTime(parseInt(dbconfigProperties.getProperty("cpool.maxIdleTime")));
        dataSource.setAcquireIncrement(parseInt(dbconfigProperties.getProperty("cpool.acquireIncrement")));
        dataSource
                .setMaxIdleTimeExcessConnections(parseInt(dbconfigProperties.getProperty("cpool.maxIdleTimeExcessConnections")));
        return dataSource;
    }
}
