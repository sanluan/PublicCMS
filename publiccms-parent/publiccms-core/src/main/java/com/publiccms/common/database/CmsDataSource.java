package com.publiccms.common.database;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.datasource.MultiDataSource;
import com.publiccms.common.tools.DatabaseUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * CmsDataSource
 * 
 */
public class CmsDataSource extends MultiDataSource {
    /**
     * 
     */
    public static final String DATABASE_CONFIG_FILENAME = "/database.properties";
    /**
     * 
     */
    public static final String DEFAULT_DATABASE_NAME = "default";
    /**
     * 
     */
    public static final String DATABASE_CONFIG_TEMPLATE = "config/database-template.properties";
    private static volatile CmsDataSource cmsDataSource;
    private String dbconfigFilePath;
    private static boolean initialized = false;

    private Map<Object, Object> dataSources = new HashMap<>();

    /**
     * @param filepath
     */
    public CmsDataSource(String filepath) {
        dbconfigFilePath = filepath;
        cmsDataSource = this;
    }

    /**
     * @param configFilePath
     * @return database config
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Properties loadDatabaseConfig(String configFilePath) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        File file = new File(configFilePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
        }
        return properties;
    }

    /**
     * @param properties
     * @return database source
     * @throws IOException
     * @throws PropertyVetoException
     */
    public static DataSource getDataSource(Properties properties) throws IOException, PropertyVetoException {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty("jdbc.driverClassName"));
        config.setJdbcUrl(properties.getProperty("jdbc.url"));
        config.setUsername(properties.getProperty("jdbc.username"));
        config.setPassword(DatabaseUtils.getPassword(properties));
        config.setAutoCommit(Boolean.parseBoolean(properties.getProperty("hikariCP.autoCommit")));
        config.setConnectionTimeout(Long.parseLong(properties.getProperty("hikariCP.connectionTimeout")));
        config.setMinimumIdle(Integer.parseInt(properties.getProperty("hikariCP.minPoolSize")));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("hikariCP.maxPoolSize")));
        config.setIdleTimeout(Long.parseLong(properties.getProperty("hikariCP.idleTimeout")));
        config.setMaxLifetime(Long.parseLong(properties.getProperty("hikariCP.maxLifetime")));
        return new HikariDataSource(config);
    }

    /**
     * 
     */
    public static void initDefaultDataSource() {
        if (null != cmsDataSource) {
            synchronized (cmsDataSource) {
                if (!initialized && CmsVersion.isInitialized()) {
                    try {
                        Properties properties = loadDatabaseConfig(cmsDataSource.dbconfigFilePath);
                        DataSource dataSource = getDataSource(properties);
                        Map<Object, Object> map = new HashMap<>();
                        map.put(DEFAULT_DATABASE_NAME, dataSource);
                        cmsDataSource.setTargetDataSources(map);
                        cmsDataSource.setDefaultTargetDataSource(dataSource);
                        cmsDataSource.init();
                        initialized = true;
                    } catch (IOException | PropertyVetoException e) {
                        CmsVersion.setInitialized(false);
                    }
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() {

    }

    /**
     * 
     */
    public void init() {
        super.afterPropertiesSet();
    }

    /**
     * @param name
     * @param dataSource
     */
    public void put(Object name, Object dataSource) {
        dataSources.put(name, dataSource);
        setTargetDataSources(dataSources);
        init();
    }

    /**
     * @param name
     */
    public void remove(Object name) {
        dataSources.remove(name);
        setTargetDataSources(dataSources);
        init();
    }

    /**
     * @param name
     * @return
     */
    public boolean contains(Object name) {
        return dataSources.containsKey(name);
    }
}
