package org.publiccms.common.tools;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.publiccms.common.database.CmsDataSource;

/**
 *
 * DatabaseUtils
 * 
 */
public class DatabaseUtils {

    public static Connection getConnection(String databaseConfigFile)
            throws SQLException, IOException, PropertyVetoException, ClassNotFoundException {
        Properties dbconfigProperties = CmsDataSource.loadDatabaseConfig(databaseConfigFile);
        String driverClassName = dbconfigProperties.getProperty("jdbc.driverClassName");
        String url = dbconfigProperties.getProperty("jdbc.url");
        String userName = dbconfigProperties.getProperty("jdbc.username");
        String password = dbconfigProperties.getProperty("jdbc.password");
        return getConnection(driverClassName, url, userName, password);
    }

    public static Connection getConnection(String driverClassName, String url, String userName, String password)
            throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
        return DriverManager.getConnection(url, userName, password);
    }
}
