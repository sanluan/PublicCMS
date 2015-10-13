package config.initializer;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.core.Conventions;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.IntrospectorCleanupListener;

import com.publiccms.logic.component.BridgeComponent;
import com.publiccms.logic.task.ScheduledTask;
import com.sanluan.common.servlet.ErrorToNotFoundDispatcherServlet;
import com.sanluan.common.servlet.TaskAfterInitServlet;

import config.AdminConfig;
import config.ApplicationConfig;
import config.WebConfig;

/**
 * 
 * WebInitializer Servlet3.0 工程入口类
 *
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(IntrospectorCleanupListener.class);
        ApplicationConfig.basePath = servletContext.getRealPath("/");

        registerContextLoaderListener(servletContext);
        registerDispatcherServlet("web", 1, servletContext, createServletApplicationContext(getServletConfigClasses()),
                getServletMappings(), null);
        registerDispatcherServlet("admin", 2, servletContext, createServletApplicationContext(getAdminServletConfigClasses()),
                getAdminServletMappings(), getTaskClasses());
    }

    protected WebApplicationContext createServletApplicationContext(Class<?>[] servletConfigClasses) {
        AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
        if (!ObjectUtils.isEmpty(servletConfigClasses)) {
            servletAppContext.register(servletConfigClasses);
        }
        return servletAppContext;
    }

    protected void registerDispatcherServlet(String servletName, int order, ServletContext servletContext,
            WebApplicationContext servletAppContext, String[] servletMappings, Class<TaskAfterInitServlet>[] tasksAfterServletInit) {
        Assert.notNull(servletAppContext, "createServletApplicationContext() did not return an application "
                + "context for servlet [" + servletName + "]");

        ErrorToNotFoundDispatcherServlet dispatcherServlet = new ErrorToNotFoundDispatcherServlet(servletAppContext);
        dispatcherServlet.setTaskClasses(tasksAfterServletInit);
        ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, dispatcherServlet);
        Assert.notNull(registration, "Failed to register servlet with name '" + servletName + "'."
                + "Check if there is another servlet registered under the same name.");

        registration.setLoadOnStartup(order);
        registration.addMapping(servletMappings);
        registration.setAsyncSupported(isAsyncSupported());

        Filter[] servletFilters = getServletFilters();
        if (!ObjectUtils.isEmpty(servletFilters)) {
            for (Filter filter : servletFilters) {
                registerServletFilter(servletName, servletContext, filter);
            }
        }
        customizeRegistration(registration);
    }

    protected FilterRegistration.Dynamic registerServletFilter(String servletName, ServletContext servletContext, Filter filter) {
        String filterName = Conventions.getVariableName(filter);
        Dynamic registration = servletContext.addFilter(filterName, filter);
        if (registration == null) {
            int counter = -1;
            while (counter == -1 || registration == null) {
                counter++;
                registration = servletContext.addFilter(filterName + "#" + counter, filter);
                Assert.isTrue(counter < 100, "Failed to register filter '" + filter + "'."
                        + "Could the same Filter instance have been registered already?");
            }
        }
        registration.setAsyncSupported(isAsyncSupported());
        registration.addMappingForServletNames(getDispatcherTypes(), false, servletName);
        return registration;
    }

    private EnumSet<DispatcherType> getDispatcherTypes() {
        return (isAsyncSupported() ? EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE,
                DispatcherType.ASYNC) : EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE));
    }

    @SuppressWarnings("unchecked")
    protected Class<TaskAfterInitServlet>[] getTaskClasses() {
        return new Class[] { ScheduledTask.class, BridgeComponent.class };
    }

    protected Class<?>[] getAdminServletConfigClasses() {
        return new Class[] { AdminConfig.class };
    }

    protected String[] getAdminServletMappings() {
        return new String[] { "/admin/*" };
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { ApplicationConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "*.html", "*.do", "*.json" };
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] { characterEncodingFilter };
    }
}
