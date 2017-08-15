package org.publiccms.common.servlet;

import static org.publiccms.common.constants.CommonConstants.INSTALL_LOCK_FILENAME;
import static org.publiccms.common.constants.CommonConstants.CMS_FILEPATH;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.logging.LogFactory.getLog;
import static org.publiccms.common.database.CmsDataSource.DATABASE_CONFIG_FILENAME;
import static org.publiccms.common.database.CmsDataSource.DATABASE_CONFIG_TEMPLATE;
import static org.publiccms.common.tools.DatabaseUtils.getConnection;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
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
public class InstallServlet extends HttpServlet implements Base {

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
            Map<String, Object> map = new HashMap<>();

            // 记录当前版本号
            map.put("currentVersion", CmsVersion.getVersion());

            if (null == step) {
                step = startStep;
            }

            // 2017-06-19 修改connection为局部变量，避免出现connection没有关闭的漏洞
            if (null != step) {
                map.put("versions", CmsUpgrader.VERSION_LIST);
                map.put("fromVersion", fromVersion);
                switch (step) {
                case STEP_DATABASECONFIG:
                    configDatabase(request, map);
                    break;
                case STEP_CHECKDATABASE:
                    checkDatabse(map);
                    break;
                case STEP_INITDATABASE:
                    try {
                        initDatabase(request.getParameter("useSimple"), map);
                        startCMS(map);
                    } catch (Exception e) {
                        map.put("error", e.getMessage());
                    }
                    break;
                case STEP_UPDATE:
                    try {
                        upgradeDatabase(request.getParameter("from_version"), map);
                        startCMS(map);
                    } catch (Exception e) {
                        map.put("message", "failed");
                        map.put("error", e.getMessage());
                    }
                    break;
                case STEP_START:
                    try {
                        start();
                        step = "startOk";
                    } catch (PropertyVetoException e) {
                        map.put("message", "failed");
                        map.put("error", e.getMessage());
                    }
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
            render(step, map, response);
        }
    }

    private void start() throws IOException, PropertyVetoException {
        CmsVersion.setInitialized(true);
        CmsDataSource.initDefautlDataSource();
        File file = new File(CMS_FILEPATH + INSTALL_LOCK_FILENAME);
        writeStringToFile(file, CmsVersion.getVersion(), DEFAULT_CHARSET);
        file.setReadable(true, false);
        file.setWritable(true, false);
        log.info("PublicCMS " + CmsVersion.getVersion() + " started!");
    }

    /**
     * 配置数据库
     */
    private void configDatabase(HttpServletRequest request, Map<String, Object> map) {
        Connection connection = null;
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
        } catch (Exception e) {
            map.put("error", e.getMessage());
        } finally {
            if (null != connection) {
                if (null != connection) {
                    try {
                        connection.close();
                    } catch (SQLException e1) {
                        map.put("error", e1.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 检查数据库
     * 
     * @param map
     * @throws ServletException
     * @throws IOException
     */
    private void checkDatabse(Map<String, Object> map) {
        Connection connection = null;
        try {
            String databaseConfiFile = CMS_FILEPATH + DATABASE_CONFIG_FILENAME;
            connection = getConnection(databaseConfiFile);
            map.put("message", "success");
        } catch (Exception e) {
            map.put("error", e.getMessage());
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e1) {
                    map.put("error", e1.getMessage());
                }
            }
        }
    }

    /**
     * 初始化数据库
     * 
     * @param type
     * @param map
     * @throws IOException
     */
    private void initDatabase(String type, Map<String, Object> map) throws Exception {
        Connection connection = null;
        try {
            String databaseConfiFile = CMS_FILEPATH + DATABASE_CONFIG_FILENAME;
            connection = getConnection(databaseConfiFile);
            try {
                map.put("history", install(connection, null != type));
                map.put("message", "success");
            } catch (Exception e) {
                map.put("message", "failed");
                map.put("error", e.getMessage());
            }
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e1) {
                    map.put("error", e1.getMessage());
                }
            }
        }
    }

    private String install(Connection connection, boolean useSimple) throws SQLException, IOException {
        StringWriter stringWriter = new StringWriter();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setLogWriter(null);
        runner.setErrorLogWriter(new PrintWriter(stringWriter));
        runner.setAutoCommit(true);
        try (InputStream inputStream = getClass().getResourceAsStream("/initialization/sql/initDatabase.sql");) {
            runner.runScript(new InputStreamReader(inputStream, DEFAULT_CHARSET));
        }
        if (useSimple) {
            try (InputStream simpleInputStream = getClass().getResourceAsStream("/initialization/sql/initDatabase.sql")) {
                runner.runScript(new InputStreamReader(simpleInputStream, DEFAULT_CHARSET));
            }
        }
        return stringWriter.toString();
    }

    /**
     * 升级数据库
     */
    private void upgradeDatabase(String fromVersion, Map<String, Object> map) throws Exception {
        if (CmsUpgrader.VERSION_LIST.contains(fromVersion)) {
            Connection connection = null;
            try {
                String databaseConfiFile = CMS_FILEPATH + DATABASE_CONFIG_FILENAME;
                connection = getConnection(databaseConfiFile);

                CmsUpgrader upgrader = null;
                try {
                    upgrader = new CmsUpgrader(config, connection, fromVersion);
                    upgrader.update();
                    map.put("message", "success");
                } catch (Exception e) {
                    if (null != upgrader) {
                        fromVersion = upgrader.getVersion();
                    }
                    throw e;
                }
            } finally {
                if (null != connection) {
                    try {
                        connection.close();
                    } catch (SQLException e1) {
                        map.put("error", e1.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 启动CMS
     */
    private void startCMS(Map<String, Object> map) {
        try {
            start();
        } catch (Exception e) {
            CmsVersion.setInitialized(false);
            map.put("error", e.getMessage());
        }
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
