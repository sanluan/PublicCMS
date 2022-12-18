package config.initializer;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.WebApplicationInitializer;

import com.publiccms.common.base.BaseServletInitializer;
import com.publiccms.common.constants.CommonConstants;

import config.spring.ApiConfig;

/**
 * <p>Servlet3.0 工程入口类</p>
 * <p>WebInitializer</p>
 *
 */
public class ApiInitializer extends BaseServletInitializer implements WebApplicationInitializer {// 防止jetty等追求速度的容器不扫描父类实现的接口

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { ApiConfig.class };
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
        return new String[] { "/api/*" };
    }
}
