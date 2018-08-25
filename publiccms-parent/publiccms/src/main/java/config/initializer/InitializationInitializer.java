package config.initializer;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.util.IntrospectorCleanupListener;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.proxy.UsernamePasswordAuthenticator;
import com.publiccms.common.servlet.InstallHttpRequestHandler;
import com.publiccms.common.servlet.InstallServlet;

/**
 *
 * InstallationInitializer
 *
 */
public class InitializationInitializer implements WebApplicationInitializer {
    protected final Log log = LogFactory.getLog(getClass());
    /**
     * 安装Servlet映射路径
     */
    public final static String INSTALL_SERVLET_MAPPING = "/install/";
    /**
     * 安装跳转处理器
     */
    public final static HttpRequestHandler INSTALL_HTTPREQUEST_HANDLER = new InstallHttpRequestHandler(INSTALL_SERVLET_MAPPING);

    @Override
    public void onStartup(ServletContext servletcontext) throws ServletException {
        try {
            Properties config = PropertiesLoaderUtils.loadAllProperties(CommonConstants.CMS_CONFIG_FILE);
            initProxy(config);
            CommonConstants.CMS_FILEPATH = System.getProperty("cms.filePath", config.getProperty("cms.filePath"));
            String absolutePath = new File(CommonConstants.CMS_FILEPATH).getAbsolutePath();
            File file = new File(absolutePath + CommonConstants.INSTALL_LOCK_FILENAME);
            if (file.exists()) {
                String version = FileUtils.readFileToString(file, CommonConstants.DEFAULT_CHARSET);
                if (CmsVersion.getVersion().equals(version)) {
                    CmsVersion.setInitialized(true);
                    CmsDataSource.initDefautlDataSource();
                    log.info("PublicCMS " + CmsVersion.getVersion() + " will start normally in " + absolutePath);
                } else {
                    createInstallServlet(servletcontext, config, InstallServlet.STEP_CHECKDATABASE, version);
                    log.warn("PublicCMS " + CmsVersion.getVersion() + " installer will start in " + absolutePath
                            + ", please upgrade your database!");
                }
            } else {
                createInstallServlet(servletcontext, config, null, null);
                log.warn("PublicCMS " + CmsVersion.getVersion() + " installer will start in " + absolutePath
                        + ", please configure your database information and initialize the database!");
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }
        servletcontext.addListener(IntrospectorCleanupListener.class);
    }

    private static void createInstallServlet(ServletContext servletcontext, Properties config, String startStep, String version) {
        Dynamic registration = servletcontext.addServlet("install", new InstallServlet(config, startStep, version));
        registration.setLoadOnStartup(1);
        registration.addMapping(new String[] { INSTALL_SERVLET_MAPPING });
    }

    /**
     * 代理配置
     * 
     * @param config
     * @throws IOException
     */
    private static void initProxy(Properties config) throws IOException {
        if ("true".equalsIgnoreCase(System.getProperty("cms.proxy.enable", config.getProperty("cms.proxy.enable", "false")))) {
            Properties proxyProperties = PropertiesLoaderUtils.loadAllProperties(
                    System.getProperty("cms.proxy.configFilePath", config.getProperty("cms.proxy.configFilePath")));
            for (String key : proxyProperties.stringPropertyNames()) {
                System.setProperty(key, proxyProperties.getProperty(key));
            }
            Authenticator.setDefault(new UsernamePasswordAuthenticator(config.getProperty("cms.proxy.userName"),
                    config.getProperty("cms.proxy.password")));
        }
    }
}
