package config.spring;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.publiccms.common.interceptor.WebContextInterceptor;
import com.publiccms.common.view.WebFreeMarkerViewResolver;
import com.publiccms.common.view.web.DefaultWebFreeMarkerView;
import com.publiccms.common.view.web.WebFreeMarkerView;
import com.publiccms.logic.component.cache.CacheComponent;
import com.publiccms.logic.component.template.TemplateComponent;

/**
 * 
 * WebConfig WebServlet配置类
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.publiccms.controller.web", useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(value = { Controller.class }) })
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private WebContextInterceptor webInitializingInterceptor;
    @Autowired
    private CacheComponent cacheComponent;

    /**
     * 视图层解析器
     * 
     * @return
     */
    @Bean
    public ViewResolver defaultWebViewResolver() {
        FreeMarkerViewResolver bean = new FreeMarkerViewResolver();
        bean.setOrder(1);
        bean.setViewClass(DefaultWebFreeMarkerView.class);
        bean.setPrefix("/web/");
        bean.setContentType("text/html;charset=UTF-8");
        cacheComponent.registerCachingViewResolverList(bean);
        return bean;
    }

    /**
     * 视图层解析器
     * 
     * @return
     * @throws IOException
     */
    @Bean
    public WebFreeMarkerViewResolver webViewResolver(TemplateComponent templateComponent) {
        WebFreeMarkerViewResolver bean = new WebFreeMarkerViewResolver();
        bean.setOrder(0);
        bean.setConfiguration(templateComponent.getWebConfiguration());
        bean.setViewClass(WebFreeMarkerView.class);
        bean.setContentType("text/html;charset=UTF-8");
        cacheComponent.registerCachingViewResolverList(bean);
        return bean;
    }

    /**
     * 拦截器
     * 
     * @return
     */
    @Bean
    public WebContextInterceptor webInitializingInterceptor() {
        return new WebContextInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webInitializingInterceptor);
    }
}
