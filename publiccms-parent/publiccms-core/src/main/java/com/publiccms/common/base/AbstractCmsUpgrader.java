package com.publiccms.common.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.publiccms.common.constants.CommonConstants;

/**
 *
 * AbstractCmsUpgrader
 *
 */
public abstract class AbstractCmsUpgrader {
    /**
     * 表名_ID_SEQ SEQUENCE主键策略
     */
    public static final String IDENTIFIER_GENERATOR_SEQUENCE = "com.publiccms.common.database.IDSequenceStyleGenerator";
    /**
     * ID自增主键策略
     */
    public static final String IDENTIFIER_GENERATOR_IDENTITY = "org.hibernate.id.IdentityGenerator";
    /**
     * 主键策略
     */
    public static final String IDENTIFIER_GENERATOR = IDENTIFIER_GENERATOR_IDENTITY;
    protected String version;
    protected Properties config;

    public AbstractCmsUpgrader(Properties config) {
        this.config = config;
    }

    /**
     * @param connection
     * @param fromVersion
     * @throws SQLException
     * @throws IOException
     */
    public abstract void update(Connection connection, String fromVersion) throws SQLException, IOException;

    /**
     * @return version list
     */
    public abstract List<String> getVersionList();

    /**
     * @return default port
     */
    public abstract int getDefaultPort();

    /**
     * @param dbconfig
     * @param host
     * @param port
     * @param database
     * @throws IOException
     * @throws URISyntaxException
     */
    public abstract void setDataBaseUrl(Properties dbconfig, String host, String port, String database)
            throws IOException, URISyntaxException;

    protected void runScript(Connection connection, String fromVersion, String toVersion) throws SQLException, IOException {
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setLogWriter(null);
        runner.setErrorLogWriter(null);
        runner.setAutoCommit(true);
        try (InputStream inputStream = getClass()
                .getResourceAsStream("/initialization/upgrade/" + fromVersion + "-" + toVersion + ".sql");) {
            if (null != inputStream) {
                runner.runScript(new InputStreamReader(inputStream, CommonConstants.DEFAULT_CHARSET));
            }
        }
        version = toVersion;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }
}
