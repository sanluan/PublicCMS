package config.initializer;

import org.springframework.web.WebApplicationInitializer;

import com.publiccms.common.base.BaseServletInitializer;

import config.spring.ApiConfig;

/**
 * <p>Servlet3.0 工程入口类</p>
 * <p>WebInitializer</p>
 *
 */
public class ApiInitializer extends BaseServletInitializer implements WebApplicationInitializer {

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { ApiConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/api/*" };
    }
}
