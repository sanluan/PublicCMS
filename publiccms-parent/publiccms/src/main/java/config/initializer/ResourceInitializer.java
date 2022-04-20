package config.initializer;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.core.Conventions;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.publiccms.common.constants.CommonConstants;

/**
 * 
 * ResourceInitializer Servlet3.0 工程入口类
 *
 */
public class ResourceInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        Dynamic registration = servletContext.addServlet("defaultServlet", new HttpRequestHandlerServlet());
        registration.setLoadOnStartup(1);
        registration.addMapping(new String[] { "/resource/*" });
        Dynamic webfileRegistration = servletContext.addServlet("webfileServlet", new HttpRequestHandlerServlet());
        webfileRegistration.setLoadOnStartup(0);
        webfileRegistration.addMapping(new String[] { "/webfile/*", "/favicon.ico" });
        Filter[] filters = getServletFilters();
        if (!ObjectUtils.isEmpty(filters)) {
            for (Filter filter : filters) {
                registerServletFilter(servletContext, filter, new String[] { "defaultServlet", "webfileServlet" });
            }
        }
    }

    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter,
            String[] servletNames) {
        String filterName = Conventions.getVariableName(filter);
        FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
        if (null == registration) {
            int counter = -1;
            while (-1 == counter || null == registration) {
                counter++;
                registration = servletContext.addFilter(filterName + "#" + counter, filter);
                Assert.isTrue(counter < 100, "Failed to register filter '" + filter + "'."
                        + "Could the same Filter instance have been registered already?");
            }
        }
        registration.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE),
                false, servletNames);
        return registration;
    }

    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(CommonConstants.DEFAULT_CHARSET_NAME);
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] { characterEncodingFilter };
    }
}
