package config.boot;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.AbstractEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.publiccms.common.constants.CommonConstants;

import config.initializer.AdminInitializer;
import config.initializer.ApiInitializer;
import config.initializer.InitializationInitializer;
import config.initializer.ResourceInitializer;
import config.initializer.WebInitializer;
import config.spring.CmsConfig;

/**
 *
 * SprintBootApplication
 * 
 */
@Configuration
@Import(CmsConfig.class)
public class SprintBootApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        CommonConstants.applicationContext = SpringApplication.run(SprintBootApplication.class, args);
    }

    /**
     * @return servlet container
     */
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        String server = System.getProperty("cms.server");
        AbstractEmbeddedServletContainerFactory factory = null;
        if ("jetty".equalsIgnoreCase(server)) {
            factory = new JettyEmbeddedServletContainerFactory();
        } else if ("undertow".equalsIgnoreCase(server)) {
            factory = new UndertowEmbeddedServletContainerFactory();
        } else {
            factory = new TomcatEmbeddedServletContainerFactory();
        }
        factory.setPort(Integer.valueOf(System.getProperty("cms.port", "8080")));
        factory.setContextPath(System.getProperty("cms.contextPath", ""));
        factory.setDisplayName("PublicCMS");
        factory.setSessionTimeout(20, TimeUnit.MINUTES);
        return factory;
    }

    /**
     * @return web servlet initializer
     */
    @Bean
    public ServletContextInitializer webInitializer() {
        return new ServletContextInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                new WebInitializer().onStartup(servletContext);
            }

        };
    }

    /**
     * @return admin servlet initializer
     */
    @Bean
    public ServletContextInitializer adminInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                new AdminInitializer().onStartup(servletContext);
            }
        };
    }

    /**
     * @return api servlet initializer
     */
    @Bean
    public ServletContextInitializer apiInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                new ApiInitializer().onStartup(servletContext);
            }
        };
    }

    /**
     * @return install servlet initializer
     */
    @Bean
    public ServletContextInitializer installationInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                new InitializationInitializer().onStartup(servletContext);
            }
        };
    }

    /**
     * @return resource servlet initializer
     */
    @Bean
    public ServletContextInitializer resourceInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                new ResourceInitializer().onStartup(servletContext);
            }
        };
    }
}