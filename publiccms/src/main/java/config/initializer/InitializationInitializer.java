package config.initializer;

import static com.publiccms.common.initialization.InstallServlet.STEP_CHECKDATABASE;
import static com.publiccms.common.tools.DatabaseUtils.getDataSource;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.Authenticator;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.initialization.InstallServlet;
import com.sanluan.common.proxy.UsernamePasswordAuthenticator;

/**
 *
 * InstallationInitializer
 *
 */
public class InitializationInitializer implements WebApplicationInitializer {
    public static final String CMS_CONFIG_FILE = "cms.properties";
    public static final String PROPERTY_NAME_DATABASE = "cms.database.configFilePath";

    @Override
    public void onStartup(ServletContext servletcontext) throws ServletException {
        ComboPooledDataSource dataSource = null;
        try {
            Properties properties = loadAllProperties(CMS_CONFIG_FILE);
            if ("true".equalsIgnoreCase(properties.getProperty("cms.proxy.enable", "false"))) {
                Properties proxyProperties = loadAllProperties("cms.proxy.configFilePath");
                for (String key : proxyProperties.stringPropertyNames()) {
                    System.setProperty(key, proxyProperties.getProperty(key));
                }
                Authenticator.setDefault(new UsernamePasswordAuthenticator("cms.proxy.userName", "cms.proxy.password"));
            }
            dataSource = getDataSource(properties.getProperty(PROPERTY_NAME_DATABASE));
            dataSource.getConnection();
            dataSource.close();
            if ("true".equalsIgnoreCase(properties.getProperty("cms.autoInstall", "false"))) {
                createInstallServlet(servletcontext, STEP_CHECKDATABASE);
            }else{
                CmsVersion.setInitialized(true);
            }
        } catch (PropertyVetoException | SQLException | IOException e) {
            if (null != dataSource) {
                dataSource.close();
            }
            createInstallServlet(servletcontext, null);
        }
    }

    private void createInstallServlet(ServletContext servletcontext, String startStep) {
        Dynamic registration = servletcontext.addServlet("install", new InstallServlet(startStep));
        registration.setLoadOnStartup(1);
        registration.addMapping(new String[] { "/install/*" });
    }

}
