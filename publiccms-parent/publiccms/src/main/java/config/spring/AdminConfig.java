package config.spring;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.publiccms.common.handler.FullBeanNameGenerator;
import com.publiccms.common.interceptor.AdminContextInterceptor;
import com.publiccms.common.interceptor.CsrfInterceptor;
import com.publiccms.common.view.AdminFreeMarkerView;
import com.publiccms.logic.component.cache.CacheComponent;
import com.publiccms.logic.component.template.TemplateComponent;

/**
 * AdminServlet配置类
 * 
 * AdminConfig
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.publiccms.controller.admin", useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(value = { Controller.class }) }, nameGenerator = FullBeanNameGenerator.class)
public class AdminConfig implements WebMvcConfigurer {
    /**
     * 管理后台上下文路径 Management Context Path
     */
    public static final String ADMIN_CONTEXT_PATH = "/admin";

    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private AdminContextInterceptor adminInterceptor;

    @Bean
    public LocaleResolver localeResolver(Environment env) {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieName("cms.locale");
        localeResolver.setCookieMaxAge(30 * 24 * 3600);
        String defaultLocale = env.getProperty("cms.defaultLocale");
        if (!"auto".equalsIgnoreCase(defaultLocale)) {
            localeResolver.setDefaultLocale(Locale.forLanguageTag(defaultLocale));
        }
        return localeResolver;
    }

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
     * @param templateComponent
     * 
     * @return admin servlet interceptor
     */
    @Bean
    public AdminContextInterceptor adminInterceptor(TemplateComponent templateComponent) {
        templateComponent.setAdminContextPath(ADMIN_CONTEXT_PATH);
        AdminContextInterceptor bean = new AdminContextInterceptor();
        bean.setAdminContextPath(ADMIN_CONTEXT_PATH);
        bean.setLoginUrl("/login.html");
        bean.setUnauthorizedUrl("/common/unauthorizedUrl.html");
        bean.setLoginJsonUrl("/common/ajaxTimeout.html");
        bean.setNeedNotLoginUrls(new String[] { "/logout", "/changeLocale", "/login" });
        bean.setNeedNotAuthorizedUrls(new String[] { "/index", "/main", "/menus", "/common/", "/isWeak" });
        return bean;
    }

    @Bean
    public CsrfInterceptor csrfInterceptor() {
        return new CsrfInterceptor(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(csrfInterceptor());
        registry.addInterceptor(adminInterceptor);
    }
}
