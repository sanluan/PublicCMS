package config.initializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.IntrospectorCleanupListener;

import com.sanluan.common.servlet.ErrorToNotFoundDispatcherServlet;

import config.AdminConfig;
import config.ApplicationConfig;

/**
 * 
 * AdminInitializer Servlet3.0 工程入口类
 *
 */
public class AdminInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(IntrospectorCleanupListener.class);
        ApplicationConfig.basePath = servletContext.getRealPath("/");
        super.onStartup(servletContext);
    }

    @Override
    protected DispatcherServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        return new ErrorToNotFoundDispatcherServlet(servletAppContext);
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { AdminConfig.class };
    }

    protected String getServletName() {
        return "admin";
    }

    protected String[] getServletMappings() {
        return new String[] { "/admin/*" };
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { ApplicationConfig.class };
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] { characterEncodingFilter };
    }
}
