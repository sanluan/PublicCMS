package com.publiccms.common.database;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.publiccms.common.base.AbstractCmsUpgrader;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * CmsUpgrader
 *
 */
public class CmsUpgrader extends AbstractCmsUpgrader {

    /**
     * 主键策略
     */
    public static final String IDENTIFIER_GENERATOR = IDENTIFIER_GENERATOR_IDENTITY;
    /**
     *
     */
    private static final String VERSION_20170708 = "V2017.0708";
    private static final String VERSION_20180210 = "V4.0.20180210";
    private static final String VERSION_180707 = "V4.0.180707";
    private static final String VERSION_180825 = "V4.0.180825";
    private static final String VERSION_181024 = "V4.0.181024";
    private static final String VERSION_190312 = "V4.0.190312";
    private static final String VERSION_2019 = "V2019";
    private static final String VERSION_202004 = "V4.0.202004";
    private static final String VERSION_202011 = "V4.0.202011";
    private static final String VERSION_202107 = "V4.0.202107";
    private static final String VERSION_202204 = "V4.0.202204";
    private static final String VERSION_202302 = "V4.0.202302";
    private static final String VERSION5_202302 = "V5.202302";
    private static final String VERSION_202406 = "V4.0.202406";

    private static final List<String> OLD_DATABASE_CONFIG_VERSION_LIST = Arrays.asList(VERSION_20170708, VERSION_20180210,
            VERSION_180707, VERSION_180825, VERSION_181024);
    /**
     *
     */
    private static final List<String> VERSION_LIST = Arrays.asList(VERSION_20170708, VERSION_20180210, VERSION_180707,
            VERSION_180825, VERSION_181024, VERSION_190312, VERSION_2019, VERSION_202004, VERSION_202011, VERSION_202107,
            VERSION_202204, VERSION_202302, VERSION5_202302);

    /**
     * @throws SQLException
     * @throws IOException
     */
    @Override
    public void update(StringWriter stringWriter, Connection connection, String fromVersion) throws SQLException, IOException {
        switch (fromVersion) {
        case VERSION_20170708:
            runScript(stringWriter, connection, VERSION_20170708, VERSION_20180210);
        case VERSION_20180210:
            runScript(stringWriter, connection, VERSION_20180210, VERSION_180707);
        case VERSION_180707:
            updateMetadata(stringWriter, connection);
            runScript(stringWriter, connection, VERSION_180707, VERSION_180825);
        case VERSION_180825:
            runScript(stringWriter, connection, VERSION_180825, VERSION_180825);
        case VERSION_181024:
            runScript(stringWriter, connection, VERSION_181024, VERSION_190312);
        case VERSION_2019:
        case VERSION_190312:
            runScript(stringWriter, connection, VERSION_190312, VERSION_202004);
        case VERSION_202004:
            runScript(stringWriter, connection, VERSION_202004, VERSION_202011);
        case VERSION_202011:
            runScript(stringWriter, connection, VERSION_202011, VERSION_202107);
        case VERSION_202107:
            updateCategoryType(stringWriter, connection);
            runScript(stringWriter, connection, VERSION_202107, VERSION_202204);
        case VERSION_202204:
            runScript(stringWriter, connection, VERSION_202204, VERSION_202302);
            updateSiteConfig(stringWriter, connection);
        case VERSION_202302:
        case VERSION5_202302:
            runScript(stringWriter, connection, VERSION_202302, VERSION_202406);
        }
    }

    @Override
    public void setDataBaseUrl(Properties dbconfig, String host, String port, String database, String timeZone)
            throws IOException, URISyntaxException {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://");
        sb.append(host);
        sb.append(":");
        if (CommonUtils.empty(port)) {
            sb.append(getDefaultPort());
        } else {
            sb.append(port);
        }
        sb.append("/");
        sb.append(database);
        sb.append("?characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true&useAffectedRows=true");
        if (CommonUtils.notEmpty(timeZone)) {
            sb.append("&serverTimezone=GMT");
            if (!"Z".equalsIgnoreCase(timeZone)) {
                sb.append(CommonUtils.encodeURI(timeZone));
            }
        }
        dbconfig.setProperty("jdbc.url", sb.toString());
        dbconfig.setProperty("jdbc.driverClassName", "com.mysql.cj.jdbc.Driver");
    }

    @Override
    public List<String> getVersionList() {
        return VERSION_LIST;
    }

    @Override
    public int getDefaultPort() {
        return 3306;
    }

    @Override
    public List<String> getOldDatabaseConfigVersionList() {
        return OLD_DATABASE_CONFIG_VERSION_LIST;
    }

}
