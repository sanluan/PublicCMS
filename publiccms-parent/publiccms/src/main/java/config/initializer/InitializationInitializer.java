package config.initializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.util.Properties;

import javax.annotation.Priority;
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
import com.publiccms.common.constants.Constants;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.handler.UsernamePasswordAuthenticator;
import com.publiccms.common.servlet.InstallHttpRequestHandler;
import com.publiccms.common.servlet.InstallServlet;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * InstallationInitializer
 *
 */
@Priority(0)
public class InitializationInitializer implements WebApplicationInitializer {
    protected static final Log log = LogFactory.getLog(InitializationInitializer.class);
    /**
     * 安装Servlet映射路径
     */
    public static final String INSTALL_SERVLET_MAPPING = "/install/";
    /**
     * 安装跳转处理器
     */
    public static final HttpRequestHandler INSTALL_HTTPREQUEST_HANDLER = new InstallHttpRequestHandler(INSTALL_SERVLET_MAPPING);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        initLogManager();
        try {
            Properties config = PropertiesLoaderUtils.loadAllProperties(CommonConstants.CMS_CONFIG_FILE);
            initProxy(config);
            if (null == CommonConstants.CMS_FILEPATH) {
                initFilePath(CommonUtils.getConfig(config, "cms.filePath"), System.getProperty("user.dir"));
            }
            File file = new File(CommonUtils.joinString(CommonConstants.CMS_FILEPATH, CommonConstants.INSTALL_LOCK_FILENAME));
            if (file.exists()) {
                String version = FileUtils.readFileToString(file, Constants.DEFAULT_CHARSET_NAME);
                if (CmsVersion.getVersion().equals(version)
                        || version.contains(".") && CmsVersion.getVersion().substring(CmsVersion.getVersion().lastIndexOf("."))
                                .equals(version.substring(version.lastIndexOf(".")))) {
                    if (!CmsVersion.getVersion().equals(version)) {
                        try (FileOutputStream outputStream = new FileOutputStream(file)) {
                            outputStream.write(CmsVersion.getVersion().getBytes(Constants.DEFAULT_CHARSET));
                        }
                    }
                    CmsVersion.setInitialized(true);
                    CmsDataSource.initDefaultDataSource();
                    log.info(CommonUtils.joinString("PublicCMS ", CmsVersion.getVersion(), " will start normally in ",
                            CommonConstants.CMS_FILEPATH));
                } else {
                    createInstallServlet(servletContext, InstallServlet.STEP_CHECKDATABASE, version);
                    log.warn(CommonUtils.joinString("PublicCMS ", CmsVersion.getVersion(), " installer will start in ",
                            CommonConstants.CMS_FILEPATH, ", please upgrade your database!"));
                }
            } else {
                createInstallServlet(servletContext, null, null);
                log.warn(CommonUtils.joinString("PublicCMS ", CmsVersion.getVersion(), " installer will start in ",
                        CommonConstants.CMS_FILEPATH,
                        ", please configure your database information and initialize the database!"));
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }
        servletContext.addListener(IntrospectorCleanupListener.class);
    }

    /**
     * 检查CMS路径变量
     *
     * @param filePath
     * @param defaultPath
     *
     */
    public static void initFilePath(String filePath, String defaultPath) {
        File file = new File(filePath);
        try {
            file.mkdirs();
        } catch (Exception e) {
        }
        if (!file.exists()) {
            log.warn(CommonUtils.joinString("PublicCMS ", CmsVersion.getVersion(),
                    " the cms.filePath parameter is invalid , try to use the temporary directory."));
            file = new File(defaultPath, "data/publiccms");
        }
        CommonConstants.CMS_FILEPATH = file.getAbsolutePath();
    }

    private static void createInstallServlet(ServletContext servletcontext, String startStep, String version) {
        Dynamic registration = servletcontext.addServlet("install", new InstallServlet(startStep, version));
        registration.setLoadOnStartup(1);
        registration.addMapping(INSTALL_SERVLET_MAPPING);
    }

    /**
     * 日志配置
     */
    public static void initLogManager() {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    }

    /**
     * 代理配置
     *
     * @param config
     * @throws IOException
     */
    private static void initProxy(Properties config) throws IOException {
        if ("true".equalsIgnoreCase(CommonUtils.getConfig(config, "cms.proxy.enable"))) {
            Properties proxyProperties = PropertiesLoaderUtils
                    .loadAllProperties(CommonUtils.getConfig(config, "cms.proxy.configFilePath"));
            for (String key : proxyProperties.stringPropertyNames()) {
                System.setProperty(key, proxyProperties.getProperty(key));
            }
            Authenticator.setDefault(new UsernamePasswordAuthenticator(CommonUtils.getConfig(config, "cms.proxy.userName"),
                    CommonUtils.getConfig(config, "cms.proxy.password")));
        }
    }
}
