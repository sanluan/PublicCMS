package config;

import java.util.HashMap;
import java.util.Map;

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

import com.publiccms.common.interceptor.web.WebContextInterceptor;
import com.publiccms.common.view.web.WebFreeMarkerView;
import com.publiccms.logic.component.CacheComponent;

/**
 * 
 * WebConfig WebServlet配置类
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.publiccms.views.controller.web", useDefaultFilters = false, includeFilters = { @ComponentScan.Filter(value = { Controller.class }) })
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private CacheComponent cacheComponent;

    /**
     * 视图层解决方案
     * 
     * @return
     */
    @Bean
    public ViewResolver viewResolver() {
        FreeMarkerViewResolver bean = new FreeMarkerViewResolver();
        bean.setViewClass(WebFreeMarkerView.class);
        bean.setPrefix(CacheComponent.TEMPLATE_PREFIX);
        bean.setSuffix(".html");
        bean.setContentType("text/html;charset=UTF-8");
        return bean;
    }

    /**
     * 拦截器
     * 
     * @return
     */
    @Bean
    public WebContextInterceptor initializingInterceptor() {
        WebContextInterceptor bean = new WebContextInterceptor();
        bean.setLoginUrl("/login.html");
        bean.setNeedLoginUrls(new String[] { "/user/" });

        cacheComponent.setCacheFileDirectory("cacheHtmlFile/");
        cacheComponent.setBasePath(ApplicationConfig.basePath);
        Map<Integer, String[]> cachePaths = new HashMap<Integer, String[]>();
        cachePaths.put(60, new String[] { "index" });
        cacheComponent.setCachePaths(cachePaths);

        return bean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initializingInterceptor());
    }
}
