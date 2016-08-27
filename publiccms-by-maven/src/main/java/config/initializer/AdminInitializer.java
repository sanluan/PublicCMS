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

import com.sanluan.common.base.Base;
import com.sanluan.common.servlet.ErrorToNotFoundDispatcherServlet;

import config.AdminConfig;
import config.ApplicationConfig;

/**
 * 
 * AdminInitializer Servlet3.0 工程入口类
 *
 */
public class AdminInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer {
    public static final String BASEPATH = "/admin";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(IntrospectorCleanupListener.class);
        ApplicationConfig.basePath = servletContext.getRealPath("/");
        super.onStartup(servletContext);
    }

    @Override
    protected DispatcherServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        ApplicationConfig.webApplicationContext = servletAppContext;
        return new ErrorToNotFoundDispatcherServlet(servletAppContext);
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { AdminConfig.class };
    }

    @Override
    protected String getServletName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { BASEPATH + "/*" };
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { ApplicationConfig.class };
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(Base.DEFAULT_CHARSET);
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] { characterEncodingFilter };
    }
}
