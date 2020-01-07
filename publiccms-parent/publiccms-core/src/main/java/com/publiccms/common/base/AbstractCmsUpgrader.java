package com.publiccms.common.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.views.pojo.entities.CmsPageData;

/**
 *
 * AbstractCmsUpgrader
 *
 */
public abstract class AbstractCmsUpgrader {
    /**
     * 表名_ID_SEQ SEQUENCE主键策略
     */
    public static final String IDENTIFIER_GENERATOR_SEQUENCE = "com.publiccms.common.datasource.IDSequenceStyleGenerator";
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
     * @param stringWriter
     * @param connection
     * @param fromVersion
     * @throws SQLException
     * @throws IOException
     */
    public abstract void update(StringWriter stringWriter, Connection connection, String fromVersion)
            throws SQLException, IOException;

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
     * @param timeZone
     * @throws IOException
     * @throws URISyntaxException
     */
    public abstract void setDataBaseUrl(Properties dbconfig, String host, String port, String database, String timeZone)
            throws IOException, URISyntaxException;

    protected void updateMetadata(StringWriter stringWriter, Connection connection) {
        try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from sys_site");) {
            while (rs.next()) {
                String filePath = CommonConstants.CMS_FILEPATH + CommonConstants.SEPARATOR + SiteComponent.TEMPLATE_PATH
                        + CommonConstants.SEPARATOR + SiteComponent.SITE_PATH_PREFIX + rs.getString("id")
                        + CommonConstants.SEPARATOR + MetadataComponent.METADATA_FILE;
                File file = new File(filePath);
                try {
                    Map<String, CmsPageData> dataMap = CommonConstants.objectMapper.readValue(file, CommonConstants.objectMapper
                            .getTypeFactory().constructMapLikeType(HashMap.class, String.class, CmsPageData.class));
                    try {
                        CommonConstants.objectMapper.writeValue(new File(CommonConstants.CMS_FILEPATH + CommonConstants.SEPARATOR
                                + SiteComponent.TEMPLATE_PATH + CommonConstants.SEPARATOR + SiteComponent.SITE_PATH_PREFIX
                                + rs.getString("id") + CommonConstants.SEPARATOR + MetadataComponent.DATA_FILE), dataMap);
                    } catch (IOException e) {
                        stringWriter.write(e.getMessage());
                        stringWriter.write(System.lineSeparator());
                    }
                } catch (IOException | ClassCastException e) {
                    stringWriter.write(e.getMessage());
                    stringWriter.write(System.lineSeparator());
                }
            }
        } catch (SQLException e) {
            stringWriter.write(e.getMessage());
            stringWriter.write(System.lineSeparator());
            e.printStackTrace();
        }
    }

    protected void runScript(StringWriter stringWriter, Connection connection, String fromVersion, String toVersion)
            throws SQLException, IOException {
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setLogWriter(null);
        runner.setErrorLogWriter(new PrintWriter(stringWriter));
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
