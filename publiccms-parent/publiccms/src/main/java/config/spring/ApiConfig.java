package config.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.publiccms.common.handler.FullBeanNameGenerator;
import com.publiccms.common.interceptor.CorsInterceptor;

/**
 * 
 * ApiConfig ApiServlet配置类
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.publiccms.controller.api", useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(value = { Controller.class }) }, nameGenerator = FullBeanNameGenerator.class)
public class ApiConfig implements WebMvcConfigurer {
    @Autowired
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor);
    }
}
