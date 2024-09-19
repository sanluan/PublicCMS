package boot;

import java.time.Duration;


import org.apache.catalina.valves.RemoteIpValve;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
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
 * SpringBootApplication
 * 
 */
@Configuration
@Import(CmsConfig.class)
public class SpringBootApplication {

	/**
     * 在idea中通过main方法启动需要将工作目录改为 publiccms 模块所在的路径，否则静态资源不能加载
     * ideaのmainメソッドから始めるには、作業ディレクトリをpubliccmsモジュールが配置されているパスに変更する必要があり、そうしないと静的リソースをロードできません
     * To start through the main method in idea, you need to change the working directory to the path where the publiccms module is located, otherwise the static resources cannot be loaded
     * https://www.publiccms.com/question/2018/02-13/376.html
     * 
     * @param args
     */
    public static void main(String[] args) {
        CommonConstants.applicationContext = SpringApplication.run(SpringBootApplication.class, args);
    }

    /**
     * @return servlet container
     */
    @Bean
    public ServletWebServerFactory servletContainer() {
        String server = System.getProperty("cms.server");
        AbstractServletWebServerFactory factory = null;
        if ("jetty".equalsIgnoreCase(server)) {
            factory = new JettyServletWebServerFactory();
        } else if ("undertow".equalsIgnoreCase(server)) {
            factory = new UndertowServletWebServerFactory();
        } else {
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
            RemoteIpValve valve = new RemoteIpValve();
            valve.setProtocolHeader("X-Forwarded-Proto");
            tomcat.addEngineValves(valve);
            factory = tomcat;
        }
        factory.setPort(Integer.valueOf(System.getProperty("cms.port", "8080")));
        factory.setContextPath(System.getProperty("cms.contextPath", "/publiccms"));
        factory.setDisplayName("PublicCMS");
        factory.setRegisterDefaultServlet(true);
        factory.getSession().setTimeout(Duration.ofMinutes(20));
        return factory;
    }

    /**
     * @return web servlet initializer
     */
    @Bean
    public ServletContextInitializer webInitializer() {
        return servletContext -> new WebInitializer().onStartup(servletContext);
    }

    /**
     * @return admin servlet initializer
     */
    @Bean
    public ServletContextInitializer adminInitializer() {
        return servletContext -> new AdminInitializer().onStartup(servletContext);
    }

    /**
     * @return api servlet initializer
     */
    @Bean
    public ServletContextInitializer apiInitializer() {
        return servletContext -> new ApiInitializer().onStartup(servletContext);
    }

    /**
     * @return install servlet initializer
     */
    @Bean
    public ServletContextInitializer installationInitializer() {
        return servletContext -> new InitializationInitializer().onStartup(servletContext);
    }

    /**
     * @return resource servlet initializer
     */
    @Bean
    public ServletContextInitializer resourceInitializer() {
        return servletContext -> new ResourceInitializer().onStartup(servletContext);
    }
}