package com.publiccms.common.initialization;

import static com.publiccms.common.tools.DatabaseUtils.getDataSource;
import static config.initializer.InitializationInitializer.CMS_CONFIG_FILE;
import static config.initializer.InitializationInitializer.PROPERTY_NAME_DATABASE;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;

import java.beans.PropertyVetoException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.datasource.CmsDataSource;
import com.publiccms.common.initialization.upgrade.CmsUpgrader;
import com.sanluan.common.base.Base;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class InstallServlet extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String STEP_CHECKDATABASE = "checkDataBase";
    public static final String STEP_DATABASECONFIG = "dataBaseConfig";
    public static final String STEP_INITDATABASE = "initDatabase";
    public static final String STEP_UPDATE = "update";
    public static final String STEP_START = "start";
    private ComboPooledDataSource dataSource;
    private freemarker.template.Configuration freemarkerConfiguration;
    private String startStep;

    public InstallServlet(String startStep) {
        this.startStep = startStep;
        this.freemarkerConfiguration = new freemarker.template.Configuration(freemarker.template.Configuration.getVersion());
        freemarkerConfiguration.setClassForTemplateLoading(getClass(), "");
        freemarkerConfiguration.setDefaultEncoding("utf-8");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (CmsVersion.isInitialized()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            String step = request.getParameter("step");
            Map<String, Object> map = new HashMap<String, Object>();
            if (null == step) {
                step = startStep;
            }
            if (null != step) {
                map.put("versions", CmsUpgrader.VERSION_LIST);
                switch (step) {
                case STEP_DATABASECONFIG:
                    try {
                        Properties properties = loadAllProperties(CMS_CONFIG_FILE);
                        String databaseConfiFile = properties.getProperty(PROPERTY_NAME_DATABASE);
                        saveDataBaseConfig(databaseConfiFile, request.getParameter("host"), request.getParameter("port"),
                                request.getParameter("database"), request.getParameter("username"),
                                request.getParameter("password"));
                        checkDataBaseConfig(databaseConfiFile);
                        map.put("message", "success");
                    } catch (PropertyVetoException | SQLException | URISyntaxException e) {
                        if (null != dataSource) {
                            dataSource.close();
                        }
                        map.put("error", e.getMessage());
                    }
                    break;
                case STEP_CHECKDATABASE:
                    try {
                        Properties properties = loadAllProperties(CMS_CONFIG_FILE);
                        String databaseConfiFile = properties.getProperty(PROPERTY_NAME_DATABASE);
                        checkDataBaseConfig(databaseConfiFile);
                        map.put("message", "success");
                    } catch (PropertyVetoException | SQLException e) {
                        if (null != dataSource) {
                            dataSource.close();
                        }
                        map.put("error", e.getMessage());
                    }
                    break;
                case STEP_INITDATABASE:
                    if (null != dataSource) {
                        try {
                            map.put("history", install("mysql", null != request.getParameter("useSimple")));
                            map.put("message", "success");
                        } catch (Exception e) {
                            map.put("message", "failed");
                            map.put("error", e.getMessage());
                        }
                    } else {
                        map.put("message", "failed");
                        map.put("error", "dataSource is null");
                    }
                    break;
                case STEP_UPDATE:
                    String fromVersion = request.getParameter("from_version");
                    if (CmsUpgrader.VERSION_LIST.contains(fromVersion)) {
                        if (null != dataSource) {
                            CmsUpgrader upgrader = null;
                            try {
                                upgrader = new CmsUpgrader("mysql", dataSource, fromVersion);
                                upgrader.update();
                                map.put("message", "success");
                            } catch (Exception e) {
                                map.put("message", "failed");
                                map.put("error", e.getMessage());
                                if (null != upgrader) {
                                    map.put("from_version", upgrader.getVersion());
                                }
                            }
                        } else {
                            map.put("message", "failed");
                            map.put("error", "dataSource is null");
                        }
                    }
                    break;
                case STEP_START:
                    if (null != dataSource) {
                        try {
                            start();
                            dataSource.close();
                            dataSource = null;
                            response.sendRedirect("../");
                        } catch (PropertyVetoException e) {
                            CmsVersion.setInitialized(false);
                            map.put("error", e.getMessage());
                        }
                    }
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
            render(step, map, response);
        }
    }

    private void saveDataBaseConfig(String databaseConfiFile, String host, String port, String database, String username,
            String password) throws IOException, URISyntaxException {
        Properties dbconfigProperties = loadAllProperties(databaseConfiFile);
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://");
        sb.append(host);
        if (Base.notEmpty(port) && !"3306".equals(port)) {
            sb.append(":");
            sb.append(port);
        }
        sb.append("/");
        sb.append(database);
        sb.append("?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=round");
        dbconfigProperties.setProperty("jdbc.url", sb.toString());
        dbconfigProperties.setProperty("jdbc.username", username);
        dbconfigProperties.setProperty("jdbc.password", password);
        try (FileOutputStream fos = new FileOutputStream(getClass().getResource("/" + databaseConfiFile).toURI().getPath())) {
            dbconfigProperties.store(fos, null);
        }
    }

    private String install(String databaseType, boolean useSimple) throws SQLException, IOException {
        StringWriter stringWriter = new StringWriter();
        ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
        runner.setLogWriter(null);
        runner.setErrorLogWriter(new PrintWriter(stringWriter));
        runner.setAutoCommit(true);
        try (InputStream inputStream = getClass().getResourceAsStream(databaseType + "/createtables.sql");) {
            runner.runScript(new InputStreamReader(inputStream, Base.DEFAULT_CHARSET));
            if (useSimple) {
                try (InputStream simpleInputStream = getClass().getResourceAsStream(databaseType + "/simpledata.sql")) {
                    runner.runScript(new InputStreamReader(simpleInputStream, Base.DEFAULT_CHARSET));
                }
            }
        }
        return stringWriter.toString();
    }

    private void checkDataBaseConfig(String databaseConfiFile) throws SQLException, IOException, PropertyVetoException {
        if (null != dataSource) {
            dataSource.close();
            dataSource = null;
        }
        dataSource = getDataSource(databaseConfiFile);
        dataSource.getConnection();
    }

    private void start() throws IOException, PropertyVetoException {
        CmsVersion.setInitialized(true);
        CmsDataSource.initDefautlDataSource();
    }

    private void render(String step, Map<String, Object> model, HttpServletResponse response) {
        if (!response.isCommitted()) {
            try {
                Template template = freemarkerConfiguration.getTemplate(null == step ? "index.html" : step + ".html");
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html");
                template.process(model, response.getWriter());
            } catch (TemplateException | IOException e) {
            }
        }
    }
}
