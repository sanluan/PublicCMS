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
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.publiccms.common.handler.FullBeanNameGenerator;
import com.publiccms.common.interceptor.CorsInterceptor;
import com.publiccms.common.interceptor.CsrfInterceptor;
import com.publiccms.common.interceptor.WebContextInterceptor;
import com.publiccms.common.view.DefaultWebFreeMarkerView;
import com.publiccms.common.view.WebFreeMarkerView;
import com.publiccms.common.view.WebFreeMarkerViewResolver;
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
        @ComponentScan.Filter(value = { Controller.class }) }, nameGenerator = FullBeanNameGenerator.class)
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private WebContextInterceptor webInterceptor;
    @Autowired
    private CorsInterceptor corsInterceptor;
    @Autowired
    private CacheComponent cacheComponent;

    @Bean
    public LocaleResolver localeResolver(Environment env) {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieName("PUBLICCMS_LOCALE");
        localeResolver.setCookieMaxAge(30 * 24 * 3600);
        String defaultLocale = env.getProperty("cms.defaultLocale");
        if (!"auto".equalsIgnoreCase(defaultLocale)) {
            localeResolver.setDefaultLocale(Locale.forLanguageTag(defaultLocale));
        }
        return localeResolver;
    }

    /**
     * 视图层解析器
     * 
     * @return default web viewresolver
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
     * @param templateComponent
     * @return web viewresolver
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
     * @return web servlet interceptor
     */
    @Bean
    public WebContextInterceptor webInterceptor() {
        return new WebContextInterceptor();
    }

    @Bean
    public CsrfInterceptor csrfInterceptor() {
        return new CsrfInterceptor(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(csrfInterceptor());
        registry.addInterceptor(webInterceptor);
        registry.addInterceptor(corsInterceptor);
    }
}
