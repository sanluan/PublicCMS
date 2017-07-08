package org.publiccms.common.database;

import static java.lang.Integer.parseInt;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.publiccms.common.datasource.MultiDataSource;

/**
 *
 * CmsDataSource
 * 
 */
public class CmsDataSource extends MultiDataSource {
    public static final String DATABASE_CONFIG_FILENAME = "/database.properties";
    public static final String DATABASE_CONFIG_TEMPLATE = "config/database-template.properties";
    private static CmsDataSource cmsDataSource;
    private String dbconfigFilePath;

    private Map<Object, Object> dataSources = new HashMap<>();

    /**
     * @param filePath
     */
    public CmsDataSource(String filePath) {
        dbconfigFilePath = filePath;
        cmsDataSource = this;
    }

    public static Properties loadDatabaseConfig(String configFilePath) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        File file = new File(configFilePath);
        if (!file.exists() && file.getParentFile().exists()) {
            file.createNewFile();
            file.setReadable(true, false);
            file.setWritable(true, false);
        }
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
        }
        return properties;
    }

    /**
     * @param properties 
     * @return
     * @throws IOException
     * @throws PropertyVetoException
     */
    public static DataSource initDataSource(Properties properties) throws IOException, PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(properties.getProperty("jdbc.driverClassName"));
        dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
        dataSource.setUser(properties.getProperty("jdbc.username"));
        dataSource.setPassword(properties.getProperty("jdbc.password"));
        dataSource.setAutoCommitOnClose(Boolean.parseBoolean(properties.getProperty("cpool.autoCommitOnClose")));
        dataSource.setCheckoutTimeout(parseInt(properties.getProperty("cpool.checkoutTimeout")));
        dataSource.setInitialPoolSize(parseInt(properties.getProperty("cpool.minPoolSize")));
        dataSource.setMinPoolSize(parseInt(properties.getProperty("cpool.minPoolSize")));
        dataSource.setMaxPoolSize(parseInt(properties.getProperty("cpool.maxPoolSize")));
        dataSource.setMaxIdleTime(parseInt(properties.getProperty("cpool.maxIdleTime")));
        dataSource.setAcquireIncrement(parseInt(properties.getProperty("cpool.acquireIncrement")));
        dataSource.setMaxIdleTimeExcessConnections(parseInt(properties.getProperty("cpool.maxIdleTimeExcessConnections")));
        return dataSource;
    }

    /**
     * @throws IOException
     * @throws PropertyVetoException
     */
    public static void initDefautlDataSource() throws IOException, PropertyVetoException {
        Properties properties = loadDatabaseConfig(cmsDataSource.dbconfigFilePath);
        DataSource dataSource = initDataSource(properties);
        cmsDataSource.getDataSources().put("default", dataSource);
        cmsDataSource.setTargetDataSources(cmsDataSource.getDataSources());
        cmsDataSource.setDefaultTargetDataSource(dataSource);
        cmsDataSource.init();
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
    }

    /**
     * @return
     */
    public Map<Object, Object> getDataSources() {
        return dataSources;
    }
}
