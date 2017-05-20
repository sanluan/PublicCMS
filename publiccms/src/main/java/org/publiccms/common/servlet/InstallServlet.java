package org.publiccms.common.servlet;

import static config.initializer.InitializationInitializer.INSTALL_LOCK_FILENAME;
import static config.spring.CmsConfig.CMS_FILEPATH;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.logging.LogFactory.getLog;
import static org.publiccms.common.database.CmsDataSource.DATABASE_CONFIG_FILENAME;
import static org.publiccms.common.database.CmsDataSource.DATABASE_CONFIG_TEMPLATE;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;
import static org.publiccms.common.tools.DatabaseUtils.getConnection;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.publiccms.common.constants.CmsVersion;
import org.publiccms.common.database.CmsDataSource;
import org.publiccms.common.database.CmsUpgrader;

import com.publiccms.common.base.Base;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 *
 * InstallServlet
 *
 */
public class InstallServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public static final String STEP_CHECKDATABASE = "checkDataBase";
    /**
     *
     */
    public static final String STEP_DATABASECONFIG = "dataBaseConfig";
    /**
     *
     */
    public static final String STEP_INITDATABASE = "initDatabase";
    /**
     *
     */
    public static final String STEP_UPDATE = "update";
    /**
     *
     */
    public static final String STEP_START = "start";

    private final Log log = getLog(getClass());

    private Connection connection;
    private freemarker.template.Configuration freemarkerConfiguration;
    private String startStep;
    private String fromVersion;
    private Properties config;

    /**
     * @param config
     * @param startStep
     * @param fromVersion
     */
    public InstallServlet(Properties config, String startStep, String fromVersion) {
        this.config = config;
        this.startStep = startStep;
        this.fromVersion = fromVersion;
        this.freemarkerConfiguration = new freemarker.template.Configuration(freemarker.template.Configuration.getVersion());
        freemarkerConfiguration.setClassForTemplateLoading(getClass(), "/initialization/");
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
                map.put("fromVersion", fromVersion);
                switch (step) {
                case STEP_DATABASECONFIG:
                    try {
                        Properties dbconfig = loadAllProperties(DATABASE_CONFIG_TEMPLATE);
                        String host = request.getParameter("host");
                        String port = request.getParameter("port");
                        String database = request.getParameter("database");
                        CmsUpgrader.setDataBaseUrl(dbconfig, host, port, database);
                        dbconfig.setProperty("jdbc.username", request.getParameter("username"));
                        dbconfig.setProperty("jdbc.password", request.getParameter("password"));

                        String databaseConfiFile = CMS_FILEPATH + DATABASE_CONFIG_FILENAME;
                        File file = new File(databaseConfiFile);
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            dbconfig.store(fos, null);
                        }
                        file.setReadable(true, false);
                        file.setWritable(true, false);
                        connection = getConnection(databaseConfiFile);
                        map.put("message", "success");
                    } catch (PropertyVetoException | SQLException | URISyntaxException | ClassNotFoundException e) {
                        if (null != connection) {
                            try {
                                connection.close();
                            } catch (SQLException e1) {
                                map.put("error", e1.getMessage());
                            }
                        }
                        map.put("error", e.getMessage());
                    }
                    break;
                case STEP_CHECKDATABASE:
                    try {
                        String databaseConfiFile = CMS_FILEPATH + DATABASE_CONFIG_FILENAME;
                        connection = getConnection(databaseConfiFile);
                        map.put("message", "success");
                    } catch (PropertyVetoException | SQLException | ClassNotFoundException e) {
                        if (null != connection) {
                            try {
                                connection.close();
                            } catch (SQLException e1) {
                                map.put("error", e1.getMessage());
                            }
                        }
                        map.put("error", e.getMessage());
                    }
                    break;
                case STEP_INITDATABASE:
                    if (null != connection) {
                        try {
                            map.put("history", install(null != request.getParameter("useSimple")));
                            map.put("message", "success");
                        } catch (Exception e) {
                            map.put("message", "failed");
                            map.put("error", e.getMessage());
                        }
                    } else {
                        map.put("message", "failed");
                        map.put("error", "connection is null");
                    }
                    break;
                case STEP_UPDATE:
                    String fromVersion = request.getParameter("from_version");
                    if (CmsUpgrader.VERSION_LIST.contains(fromVersion)) {
                        if (null != connection) {
                            CmsUpgrader upgrader = null;
                            try {
                                upgrader = new CmsUpgrader(config, connection, fromVersion);
                                upgrader.update();
                                map.put("message", "success");
                            } catch (Exception e) {
                                map.put("message", "failed");
                                map.put("error", e.getMessage());
                                if (null != upgrader) {
                                    fromVersion = upgrader.getVersion();
                                }
                            }
                        } else {
                            map.put("message", "failed");
                            map.put("error", "connection is null");
                        }
                    }
                    break;
                case STEP_START:
                    if (null != connection) {
                        try {
                            start();
                            connection.close();
                            connection = null;
                            response.sendRedirect("../");
                        } catch (PropertyVetoException | SQLException e) {
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

    private String install(boolean useSimple) throws SQLException, IOException {
        StringWriter stringWriter = new StringWriter();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setLogWriter(null);
        runner.setErrorLogWriter(new PrintWriter(stringWriter));
        runner.setAutoCommit(true);
        try (InputStream inputStream = getClass().getResourceAsStream("/initialization/sql/initDatabase.sql");) {
            runner.runScript(new InputStreamReader(inputStream, Base.DEFAULT_CHARSET));
            if (useSimple) {
                try (InputStream simpleInputStream = getClass().getResourceAsStream("/initialization/sql/initDatabase.sql")) {
                    runner.runScript(new InputStreamReader(simpleInputStream, Base.DEFAULT_CHARSET));
                }
            }
        }
        return stringWriter.toString();
    }

    private void start() throws IOException, PropertyVetoException {
        CmsVersion.setInitialized(true);
        CmsDataSource.initDefautlDataSource();
        File file = new File(CMS_FILEPATH + INSTALL_LOCK_FILENAME);
        writeStringToFile(file, CmsVersion.getVersion(), Base.DEFAULT_CHARSET);
        file.setReadable(true, false);
        file.setWritable(true, false);
        log.info("PublicCMS " + CmsVersion.getVersion() + " started!");
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
