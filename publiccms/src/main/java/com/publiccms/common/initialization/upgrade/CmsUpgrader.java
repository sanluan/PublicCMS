package com.publiccms.common.initialization.upgrade;

import static com.publiccms.logic.component.site.SiteComponent.MODEL_FILE;
import static com.publiccms.logic.component.site.SiteComponent.SITE_PATH_PREFIX;
import static com.publiccms.logic.component.site.SiteComponent.TEMPLATE_PATH;
import static config.initializer.InitializationInitializer.CMS_CONFIG_FILE;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.views.pojo.CmsModel;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.api.Json;
import com.sanluan.common.base.Base;

public class CmsUpgrader extends Base implements Json {
    public final static String VERSION_20160423 = "V2016.0423";
    public final static String VERSION_20160510 = "V2016.0510";
    public final static String VERSION_20160828 = "V2016.0828";
    public final static List<String> VERSION_LIST = Arrays
            .asList(new String[] { VERSION_20160423, VERSION_20160510, VERSION_20160828 });
    private DataSource dataSource;
    private String databaseType;
    private String version;
    private Properties properties;

    public CmsUpgrader(String databaseType, DataSource dataSource, String version) throws Exception {
        this.databaseType = databaseType;
        this.dataSource = dataSource;
        properties = loadAllProperties(CMS_CONFIG_FILE);
        this.version = version;
    }

    public void update() throws SQLException, IOException {
        switch (version) {
        case VERSION_20160423:
            runScript(VERSION_20160423, VERSION_20160510);
        case VERSION_20160510:
            runScript(VERSION_20160510, VERSION_20160828);
        case VERSION_20160828:
            updateModelToFile();
            runScript(VERSION_20160828, CmsVersion.getVersion());
            break;
        }
    }

    private void updateModelToFile() throws SQLException {
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from cms_model");) {
            while (rs.next()) {
                CmsModel entity = new CmsModel();
                String filePath = properties.getProperty("cms.filePath") + SEPARATOR + TEMPLATE_PATH + SEPARATOR
                        + SITE_PATH_PREFIX + rs.getString("site_id") + SEPARATOR + MODEL_FILE;
                File file = new File(filePath);
                file.getParentFile().mkdirs();
                Map<String, CmsModel> modelMap;
                try {
                    modelMap = objectMapper.readValue(file, new TypeReference<Map<String, CmsModel>>() {
                    });
                } catch (IOException | ClassCastException e) {
                    modelMap = new HashMap<String, CmsModel>();
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
                    List<ExtendField> extendList = new ArrayList<ExtendField>();
                    try (Connection extendFieldConnection = dataSource.getConnection();
                            Statement extendFieldStatement = connection.createStatement();
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void runScript(String fromVersion, String toVersion) throws SQLException, IOException {
        ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
        runner.setLogWriter(null);
        runner.setErrorLogWriter(null);
        runner.setAutoCommit(true);
        try (InputStream inputStream = getClass()
                .getResourceAsStream(databaseType + "/" + fromVersion + "-" + toVersion + ".sql");) {
            runner.runScript(new InputStreamReader(inputStream, "UTF-8"));
        }
        version = toVersion;
    }

    public String getVersion() {
        return version;
    }
}
