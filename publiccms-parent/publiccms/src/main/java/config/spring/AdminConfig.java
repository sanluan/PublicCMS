package config.spring;

import com.publiccms.common.interceptor.AdminContextInterceptor;
import com.publiccms.common.view.AdminFreeMarkerView;
import com.publiccms.logic.component.cache.CacheComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * AdminServlet配置类
 * 
 * AdminConfig
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.publiccms.controller.admin", useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(value = { Controller.class }) })
public class AdminConfig extends WebMvcConfigurerAdapter {
    /**
     * 管理后台路径 Management Path
     */
    public static final String ADMIN_BASE_PATH = "/admin";

    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private AdminContextInterceptor adminInitializingInterceptor;

    /**
     * 视图层解析器,SpringBoot不支持jsp的加载路径
     * 
     * @return jsp view resolver
     */
    @Bean
    public ViewResolver jspViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setOrder(1);
        bean.setPrefix("/WEB-INF/jsp/");
        bean.setSuffix(".jsp");
        bean.setContentType("text/html;charset=UTF-8");
        cacheComponent.registerCachingViewResolverList(bean);
        return bean;
    }

    /**
     * 视图层解析器
     * 
     * @return FreeMarker view resolver
     */
    @Bean
    public ViewResolver viewResolver() {
        FreeMarkerViewResolver bean = new FreeMarkerViewResolver();
        bean.setOrder(0);
        bean.setViewClass(AdminFreeMarkerView.class);
        bean.setPrefix("/admin/");
        bean.setSuffix(".html");
        bean.setContentType("text/html;charset=UTF-8");
        cacheComponent.registerCachingViewResolverList(bean);
        return bean;
    }

    /**
     * 拦截器
     * 
     * @return admin servlet interceptor
     */
    @Bean
    public AdminContextInterceptor adminInitializingInterceptor() {
        AdminContextInterceptor bean = new AdminContextInterceptor();
        bean.setAdminBasePath(ADMIN_BASE_PATH);
        bean.setLoginUrl("/login.html");
        bean.setUnauthorizedUrl("/common/unauthorizedUrl.html");
        bean.setLoginJsonUrl("/common/ajaxTimeout.html");
        bean.setNeedNotLoginUrls(new String[] { "/logout", "/changeLocale", "/common/", "/login" });
        bean.setNeedNotAuthorizedUrls(new String[] { "/index", "/main", "/menus" });
        return bean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInitializingInterceptor);
    }
}
