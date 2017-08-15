package config.initializer;

import javax.servlet.Filter;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.publiccms.common.base.Base;
import com.publiccms.common.servlet.ErrorToNotFoundDispatcherServlet;

import config.spring.ApiConfig;

/**
 * 
 * WebInitializer Servlet3.0 工程入口类
 *
 */
public class ApiInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer,Base {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected DispatcherServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        return new ErrorToNotFoundDispatcherServlet(servletAppContext);
    }

    @Override
    protected String getServletName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { ApiConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/api/*" };
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(DEFAULT_CHARSET_NAME);
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] { characterEncodingFilter };
    }
}
