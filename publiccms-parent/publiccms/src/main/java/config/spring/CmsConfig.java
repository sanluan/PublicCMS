package config.spring;

import com.publiccms.common.servlet.WebFileHttpRequestHandler;
import com.publiccms.logic.component.site.SiteComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

/**
 * Cms根配置类
 * 
 * Spring Config Class
 *
 */
@Import(ApplicationConfig.class)
public class CmsConfig {

    /**
     * 资源处理器
     * 
     * @return default servlet httprequesthandler
     */
    @Bean
    public HttpRequestHandler defaultServlet() {
        DefaultServletHttpRequestHandler bean = new DefaultServletHttpRequestHandler();
        return bean;
    }

    /**
     * 站点静态资源处理器
     * 
     * @param siteComponent 
     * @return static resource servlet httprequesthandler
     */
    @Bean
    public HttpRequestHandler webfileServlet(SiteComponent siteComponent) {
        WebFileHttpRequestHandler bean = new WebFileHttpRequestHandler(siteComponent);
        return bean;
    }
}