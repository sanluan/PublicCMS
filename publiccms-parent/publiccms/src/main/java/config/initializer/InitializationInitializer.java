package config.initializer;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.logging.LogFactory.getLog;
import static org.publiccms.common.constants.CommonConstants.CMS_FILEPATH;
import static org.publiccms.common.constants.CommonConstants.INSTALL_LOCK_FILENAME;
import static org.publiccms.common.servlet.InstallServlet.STEP_CHECKDATABASE;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.commons.logging.Log;
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
public class InitializationInitializer implements WebApplicationInitializer, Base {
    protected final Log log = getLog(getClass());
    /**
     * 配置文件
     */
    public static final String CMS_CONFIG_FILE = "cms.properties";

    @Override
    public void onStartup(ServletContext servletcontext) throws ServletException {
        Properties config = null;
        try {
            config = loadAllProperties(CMS_CONFIG_FILE);
            // 检查路径是否存在- 2017-06-17
            checkFilePath(servletcontext, config.getProperty("cms.filePath"));
            if ("true".equalsIgnoreCase(config.getProperty("cms.proxy.enable", "false"))) {
                Properties proxyProperties = loadAllProperties("cms.proxy.configFilePath");
                for (String key : proxyProperties.stringPropertyNames()) {
                    System.setProperty(key, proxyProperties.getProperty(key));
                }
                Authenticator.setDefault(new UsernamePasswordAuthenticator(config.getProperty("cms.proxy.userName"),
                        config.getProperty("cms.proxy.password")));
            }
            File file = new File(CMS_FILEPATH + INSTALL_LOCK_FILENAME);
            if (file.exists()) {
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
        } catch (IOException e) {
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

    /**
     * 检查CMS路径变量
     * 
     * @param servletcontext
     * @param defaultPath
     * @throws ServletException
     */
    private void checkFilePath(ServletContext servletcontext, String defaultPath) throws ServletException {
        CMS_FILEPATH = System.getProperty("cms.filePath", defaultPath);
        try {
            File rootFolder = new File(CMS_FILEPATH);
            if (!rootFolder.exists() && !rootFolder.mkdirs()) {
                CMS_FILEPATH = new File(servletcontext.getRealPath("/"), "cms_filepath_temp").getPath();
                log.warn("PublicCMS " + CmsVersion.getVersion() + " the cms.filePath parameter is invalid ," + CMS_FILEPATH
                        + " will be use as the default cms.filepath.");
            }
        } catch (Exception e) {
        }
    }
}
