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

import config.spring.AdminConfig;
import config.spring.ApplicationConfig;
import config.spring.CmsConfig;

/**
 * 
 * AdminInitializer Servlet3.0 工程入口类
 *
 */
public class AdminInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
        implements WebApplicationInitializer {
    public static final String BASEPATH = "/admin";

    private boolean inited;

    public AdminInitializer(boolean inited) {
        this.inited = inited;
    }

    public AdminInitializer() {
        this.inited = false;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(IntrospectorCleanupListener.class);
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
        if (inited) {
            return null;
        } else {
            return new Class[] { CmsConfig.class };
        }
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(Base.DEFAULT_CHARSET_NAME);
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] { characterEncodingFilter };
    }
}
