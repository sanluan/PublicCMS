package config.spring;

import org.publiccms.common.servlet.WebFileHttpRequestHandler;
import org.publiccms.logic.component.site.SiteComponent;
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
     * DefaultServletHttpRequestHandler
     * 
     * @return
     */
    @Bean
    public HttpRequestHandler defaultServlet() {
        DefaultServletHttpRequestHandler bean = new DefaultServletHttpRequestHandler();
        return bean;
    }

    /**
     * 站点静态页面处理器
     * 
     * DefaultServletHttpRequestHandler
     * 
     * @param siteComponent 
     * @return
     */
    @Bean
    public HttpRequestHandler webfileServlet(SiteComponent siteComponent) {
        WebFileHttpRequestHandler bean = new WebFileHttpRequestHandler(siteComponent);
        return bean;
    }
}