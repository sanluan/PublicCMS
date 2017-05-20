package config.boot;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
        SpringApplication.run(SprintBootApplication.class, args);
    }

    /**
     * @return
     */
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setPort(8080);// 设置端口
        factory.setDisplayName("PublicCMS");// 设置显示名称
        factory.setSessionTimeout(20, TimeUnit.MINUTES);// 设置session超时时间
        return factory;
    }

    /**
     * @return
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
     * @return
     */
    @Bean
    public ServletContextInitializer adminInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                new AdminInitializer(true).onStartup(servletContext);
            }
        };
    }

    /**
     * @return
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
     * @return
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
     * @return
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