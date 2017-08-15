package org.publiccms.common.database;

import static org.publiccms.common.constants.CommonConstants.CMS_FILEPATH;
import static org.publiccms.logic.component.site.SiteComponent.MODEL_FILE;
import static org.publiccms.logic.component.site.SiteComponent.SITE_PATH_PREFIX;
import static org.publiccms.logic.component.site.SiteComponent.TEMPLATE_PATH;
import static com.publiccms.common.tools.CommonUtils.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.publiccms.common.constants.CmsVersion;
import org.publiccms.views.pojo.CmsModel;
import org.publiccms.views.pojo.ExtendField;

import com.fasterxml.jackson.core.type.TypeReference;
import com.publiccms.common.base.Base;

/**
 *
 * CmsUpgrader
 *
 */
public class CmsUpgrader implements Base {
    /**
     * 主键策略
     */
    public static final String IDENTIFIER_GENERATOR = "org.hibernate.id.IdentityGenerator";
    /**
     *
     */
    public final static String VERSION_20160423 = "V2016.0423";
    /**
     *
     */
    public final static String VERSION_20160510 = "V2016.0510";
    /**
     *
     */
    public final static String VERSION_20160828 = "V2016.0828";
    /**
     *
     */
    public final static String VERSION_20170318 = "V2017.0318";
    /**
     *
     */
    public final static String VERSION_20170520 = "V2017.0520";
    /**
     *
     */
    public final static List<String> VERSION_LIST = Arrays
            .asList(new String[] { VERSION_20160423, VERSION_20160510, VERSION_20160828, VERSION_20170318, VERSION_20170520 });
    private Connection connection;
    private String version;
    @SuppressWarnings("unused")
    private Properties config;

    /**
     * @param config
     * @param connection
     * @param version
     * @throws Exception
     */
    public CmsUpgrader(Properties config, Connection connection, String version) throws Exception {
        this.connection = connection;
        this.config = config;
        this.version = version;
    }

    /**
     * @throws SQLException
     * @throws IOException
     */
    public void update() throws SQLException, IOException {
        switch (version) {
        case VERSION_20160423:
            runScript(VERSION_20160423, VERSION_20160510);
        case VERSION_20160510:
            runScript(VERSION_20160510, VERSION_20160828);
        case VERSION_20160828:
            updateModelToFile();
            runScript(VERSION_20160828, VERSION_20170318);
        case VERSION_20170318:
            runScript(VERSION_20170318, VERSION_20170520);
        case VERSION_20170520:
            runScript(VERSION_20170520, CmsVersion.getVersion());
            break;
        }
    }

    private void updateModelToFile() throws SQLException {
        try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from cms_model");) {
            while (rs.next()) {
                CmsModel entity = new CmsModel();
                String filePath = CMS_FILEPATH + SEPARATOR + TEMPLATE_PATH + SEPARATOR + SITE_PATH_PREFIX
                        + rs.getString("site_id") + SEPARATOR + MODEL_FILE;
                File file = new File(filePath);
                file.getParentFile().mkdirs();
                Map<String, CmsModel> modelMap;
                try {
                    modelMap = objectMapper.readValue(file, new TypeReference<Map<String, CmsModel>>() {
                    });
                } catch (IOException | ClassCastException e) {
                    modelMap = new HashMap<>();
                }
                entity.setId(rs.getString("id"));
                entity.setHasChild(rs.getBoolean("has_child"));
                entity.setHasFiles(rs.getBoolean("has_files"));
                entity.setHasImages(rs.getBoolean("has_images"));
                entity.setName(rs.getString("name"));
                entity.setOnlyUrl(rs.getBoolean("only_url"));
                if (null != rs.getString("parent_id")) {
                    entity.setParentId(String.valueOf(rs.getString("parent_id")));
                }
                entity.setTemplatePath((String) rs.getString("template_path"));
                if (null != rs.getString("extend_id")) {
                    List<ExtendField> extendList = new ArrayList<>();
                    try (Statement extendFieldStatement = connection.createStatement();
                            ResultSet extendFieldRs = extendFieldStatement.executeQuery(
                                    "select * from sys_extend_field where extend_id = " + rs.getString("extend_id"));) {
                        while (extendFieldRs.next()) {
                            ExtendField e = new ExtendField(extendFieldRs.getString("code"),
                                    extendFieldRs.getString("input_type"), extendFieldRs.getBoolean("required"),
                                    extendFieldRs.getString("name"), extendFieldRs.getString("description"),
                                    extendFieldRs.getString("default_value"));
                            extendList.add(e);
                        }
                    }
                    entity.setExtendList(extendList);
                }
                modelMap.put(entity.getId(), entity);
                try {
                    objectMapper.writeValue(file, modelMap);
                    file.setReadable(true, false);
                    file.setWritable(true, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void runScript(String fromVersion, String toVersion) throws SQLException, IOException {
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setLogWriter(null);
        runner.setErrorLogWriter(null);
        runner.setAutoCommit(true);
        try (InputStream inputStream = getClass()
                .getResourceAsStream("/initialization/upgrade/" + fromVersion + "-" + toVersion + ".sql");) {
            if (null != inputStream) {
                runner.runScript(new InputStreamReader(inputStream, DEFAULT_CHARSET));
            }
        }
        version = toVersion;
    }

    public static void setDataBaseUrl(Properties dbconfig, String host, String port, String database)
            throws IOException, URISyntaxException {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://");
        sb.append(host);
        if (notEmpty(port) && !"3306".equals(port)) {
            sb.append(":");
            sb.append(port);
        }
        sb.append("/");
        sb.append(database);
        sb.append("?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=round&useSSL=false");
        dbconfig.setProperty("jdbc.url", sb.toString());
        dbconfig.setProperty("jdbc.driverClassName", "com.mysql.jdbc.Driver");
    }

    /**
     * @return
     */
    public String getVersion() {
        return version;
    }
}
