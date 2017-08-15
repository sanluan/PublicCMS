package config.initializer;

import static org.publiccms.common.constants.CommonConstants.ADMIN_BASE_PATH;
import static org.publiccms.common.constants.CommonConstants.webApplicationContext;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.publiccms.common.servlet.AdminDispatcherServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.IntrospectorCleanupListener;

import com.publiccms.common.base.Base;

import config.spring.AdminConfig;
import config.spring.CmsConfig;

/**
 * 管理后台初始化
 * 
 * Management Initializer
 *
 */
public class AdminInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
        implements WebApplicationInitializer, Base {

    private boolean inited;

    /**
     * @param inited
     */
    public AdminInitializer(boolean inited) {
        this.inited = inited;
    }

    /**
     * 
     */
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
        webApplicationContext = servletAppContext;
        return new AdminDispatcherServlet(servletAppContext);
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
        return new String[] { ADMIN_BASE_PATH + "/*" };
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
        characterEncodingFilter.setEncoding(DEFAULT_CHARSET_NAME);
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] { characterEncodingFilter };
    }
}
