package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.publiccms.admin.common.interceptor.AdminContextInterceptor;
import com.publiccms.admin.common.view.AdminFreeMarkerView;

/**
 * 
 * AdminConfig AdminServlet配置类
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.publiccms.admin.views.controller", useDefaultFilters = false, includeFilters = { @ComponentScan.Filter(value = { Controller.class }) })
public class AdminConfig extends WebMvcConfigurerAdapter {

    /**
     * 视图层解决方案
     * 
     * @return
     */
    @Bean
    public ViewResolver viewResolver() {
        FreeMarkerViewResolver bean = new FreeMarkerViewResolver();
        bean.setViewClass(AdminFreeMarkerView.class);
        bean.setPrefix("/admin/");
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
    public AdminContextInterceptor initializingInterceptor() {
        AdminContextInterceptor bean = new AdminContextInterceptor();
        bean.setLoginUrl("/admin/login.html");
        bean.setUnauthorizedUrl("/admin/common/unauthorizedUrl.html");
        bean.setLoginJsonUrl("/admin/common/ajaxTimeout.html");
        bean.setNeedNotLoginUrls(new String[] { "/admin/logout", "/admin/common/", "/admin/login" });
        return bean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initializingInterceptor());
    }
}
