package config.initializer;

import static config.spring.CmsConfig.CMS_FILEPATH;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.publiccms.common.database.CmsDataSource.DATABASE_CONFIG_FILENAME;
import static org.publiccms.common.servlet.InstallServlet.STEP_CHECKDATABASE;
import static org.publiccms.common.tools.DatabaseUtils.getConnection;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.publiccms.common.constants.CmsVersion;
import org.publiccms.common.servlet.InstallServlet;
import org.springframework.web.WebApplicationInitializer;

import com.publiccms.common.base.Base;
import com.publiccms.common.proxy.UsernamePasswordAuthenticator;

/**
 *
 * InstallationInitializer
 *
 */
public class InitializationInitializer extends Base implements WebApplicationInitializer {
    /**
     * 配置文件
     */
    public static final String CMS_CONFIG_FILE = "cms.properties";
    /**
     * 安装锁
     */
    public static final String INSTALL_LOCK_FILENAME = "/install.lock";

    @Override
    public void onStartup(ServletContext servletcontext) throws ServletException {
        Properties config = null;
        Connection connection = null;
        try {
            config = loadAllProperties(CMS_CONFIG_FILE);
            CMS_FILEPATH = System.getProperty("cms.filePath", config.getProperty("cms.filePath"));
            if ("true".equalsIgnoreCase(config.getProperty("cms.proxy.enable", "false"))) {
                Properties proxyProperties = loadAllProperties("cms.proxy.configFilePath");
                for (String key : proxyProperties.stringPropertyNames()) {
                    System.setProperty(key, proxyProperties.getProperty(key));
                }
                Authenticator.setDefault(new UsernamePasswordAuthenticator(config.getProperty("cms.proxy.userName"), config.getProperty("cms.proxy.password")));
            }
            File file = new File(CMS_FILEPATH + INSTALL_LOCK_FILENAME);
            if (file.exists()) {
                connection = getConnection(CMS_FILEPATH + DATABASE_CONFIG_FILENAME);
                connection.close();
                String version = readFileToString(file, DEFAULT_CHARSET);
                if (CmsVersion.getVersion().equals(version)) {
                    CmsVersion.setInitialized(true);
                    log.info("PublicCMS " + CmsVersion.getVersion() + " will start normally in " + CMS_FILEPATH);
                } else {
                    createInstallServlet(servletcontext, config, STEP_CHECKDATABASE, version);
                    log.warn("PublicCMS " + CmsVersion.getVersion() + " installer will start in " + CMS_FILEPATH
                            + ", please upgrade your database!");
                }
            } else {
                createInstallServlet(servletcontext, config, null, null);
                log.warn("PublicCMS " + CmsVersion.getVersion() + " installer will start in " + CMS_FILEPATH
                        + ", please configure your database information and initialize the database!");
            }
        } catch (PropertyVetoException | SQLException | IOException | ClassNotFoundException e) {
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e1) {
                }
            }
            createInstallServlet(servletcontext, config, null, null);
            log.warn("PublicCMS " + CmsVersion.getVersion() + " installer will start in " + CMS_FILEPATH
                    + ", please modify your database configuration!");
        }
    }

    private void createInstallServlet(ServletContext servletcontext, Properties config, String startStep, String version) {
        Dynamic registration = servletcontext.addServlet("install", new InstallServlet(config, startStep, version));
        registration.setLoadOnStartup(1);
        registration.addMapping(new String[] { "/install/*" });
    }

}
