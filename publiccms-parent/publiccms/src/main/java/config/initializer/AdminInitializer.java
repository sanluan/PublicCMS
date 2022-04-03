package config.initializer;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.publiccms.common.base.BaseServletInitializer;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.servlet.AdminDispatcherServlet;

import config.spring.AdminConfig;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;

/**
 * <p>
 * 管理后台初始化
 * </p>
 * 
 * <p>
 * Management Initializer
 * </p>
 *
 */
public class AdminInitializer extends BaseServletInitializer implements WebApplicationInitializer {

    @Override
    protected DispatcherServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        return new AdminDispatcherServlet(servletAppContext, InitializationInitializer.INSTALL_HTTPREQUEST_HANDLER);
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { AdminConfig.class };
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        try {
            Properties config = PropertiesLoaderUtils.loadAllProperties(CommonConstants.CMS_CONFIG_FILE);
            registration.setMultipartConfig(new MultipartConfigElement(getDirPath("/tmp/"),
                    Long.parseLong(config.getProperty("cms.multipart.maxUploadSize")) * 1024 * 1024, -1L, 0));
        } catch (IOException e) {
        }
    }

    private String getDirPath(String path) {
        File dir = new File(CommonConstants.CMS_FILEPATH + path);
        dir.mkdirs();
        return dir.getAbsolutePath();
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { AdminConfig.ADMIN_CONTEXT_PATH + "/*" };
    }

}
