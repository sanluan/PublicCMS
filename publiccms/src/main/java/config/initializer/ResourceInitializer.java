package config.initializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

/**
 * 
 * ResourceInitializer Servlet3.0 工程入口类
 *
 */
public class ResourceInitializer implements WebApplicationInitializer {

    public void onStartup(ServletContext servletContext) throws ServletException {
        Dynamic registration = servletContext.addServlet("defaultServlet", new HttpRequestHandlerServlet());
        registration.setLoadOnStartup(1);
        registration.addMapping(new String[] { "/resource/*", "/favicon.ico" });
        Dynamic webfileRegistration = servletContext.addServlet("webfileServlet", new HttpRequestHandlerServlet());
        webfileRegistration.setLoadOnStartup(0);
        webfileRegistration.addMapping(new String[] { "/webfile/*" });
    }
}
