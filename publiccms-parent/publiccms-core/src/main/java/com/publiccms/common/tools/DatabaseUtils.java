package com.publiccms.common.tools;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.database.CmsDataSource;

/**
 *
 * DatabaseUtils
 * 
 */
public class DatabaseUtils {

    /**
     * @param databaseConfigFile
     * @return connection
     * @throws SQLException
     * @throws IOException
     * @throws PropertyVetoException
     * @throws ClassNotFoundException
     */
    public static Connection getConnection(String databaseConfigFile)
            throws SQLException, IOException, PropertyVetoException, ClassNotFoundException {
        Properties dbconfigProperties = CmsDataSource.loadDatabaseConfig(databaseConfigFile);
        String driverClassName = dbconfigProperties.getProperty("jdbc.driverClassName");
        String url = dbconfigProperties.getProperty("jdbc.url");
        String userName = dbconfigProperties.getProperty("jdbc.username");
        String password = dbconfigProperties.getProperty("jdbc.password");
        String encryptPassword = dbconfigProperties.getProperty("jdbc.encryptPassword");
        if (null != encryptPassword) {
            password = VerificationUtils.decrypt(VerificationUtils.base64Decode(encryptPassword), CommonConstants.ENCRYPT_KEY);
        }
        return getConnection(driverClassName, url, userName, password);
    }

    /**
     * @param driverClassName
     * @param url
     * @param userName
     * @param password
     * @return connection
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection(String driverClassName, String url, String userName, String password)
            throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
        return DriverManager.getConnection(url, userName, password);
    }
}
