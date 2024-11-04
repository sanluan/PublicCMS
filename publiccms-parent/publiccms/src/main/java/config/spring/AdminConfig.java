package config.spring;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jakarta.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.publiccms.common.api.AdminContextPath;
import com.publiccms.common.handler.FullBeanNameGenerator;
import com.publiccms.common.interceptor.AdminContextInterceptor;
import com.publiccms.common.interceptor.CsrfInterceptor;
import com.publiccms.common.view.AdminFreeMarkerView;
import com.publiccms.logic.component.cache.CacheComponent;

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

    @Resource
    private CacheComponent cacheComponent;
    @Resource
    private AdminContextInterceptor adminInterceptor;

    @Bean
    public LocaleResolver localeResolver(Environment env) {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver("cms.locale");
        localeResolver.setCookieMaxAge(Duration.ofDays(30));
        String defaultLocale = env.getProperty("cms.defaultLocale");
        if (!"auto".equalsIgnoreCase(defaultLocale)) {
            localeResolver.setDefaultLocale(Locale.forLanguageTag(defaultLocale));
        }
        return localeResolver;
    }

    /**
     * 视图层解析器
     * 
     * @return FreeMarker view resolver
     */
    @Bean
    public ViewResolver viewResolver() {
        FreeMarkerViewResolver bean = new FreeMarkerViewResolver();
        bean.setOrder(1);
        bean.setViewClass(AdminFreeMarkerView.class);
        bean.setPrefix("/admin/");
        bean.setSuffix(".html");
        bean.setExposeSessionAttributes(true);
        bean.setExposeSpringMacroHelpers(false);
        bean.setContentType("text/html;charset=UTF-8");
        cacheComponent.registerCachingViewResolverList(bean);
        return bean;
    }

    /**
     * 拦截器
     * 
     * @param adminContextPathList
     * @return admin servlet interceptor
     */
    @Bean
    public AdminContextInterceptor adminInterceptor(List<AdminContextPath> adminContextPathList) {
        if (null != adminContextPathList) {
            for (AdminContextPath adminContextPath : adminContextPathList) {
                adminContextPath.setAdminContextPath(ADMIN_CONTEXT_PATH);
            }
        }
        AdminContextInterceptor bean = new AdminContextInterceptor();
        bean.setAdminContextPath(ADMIN_CONTEXT_PATH);
        bean.setLoginUrl("/login.html");
        bean.setUnauthorizedUrl("/common/unauthorizedUrl.html");
        bean.setLoginJsonUrl("/common/ajaxTimeout.html");
        bean.setNeedNotLoginUrls(new String[] { "/changeLocale", "/login", "/getCaptchaImage", "/otp/", "/webauthn/assertion/" });
        bean.setNeedNotAuthorizedUrls(new String[] { "/index", "/main", "/logout", "/common/" });
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

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout((long) 10 * 60 * 1000);
        configurer.registerCallableInterceptors(timeoutInterceptor());
        configurer.setTaskExecutor(taskExecutor());
    }

    @Bean
    public CallableProcessingInterceptor timeoutInterceptor() {
        return new TimeoutCallableProcessingInterceptor();
    }

    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor bean = new ThreadPoolTaskExecutor();
        bean.setCorePoolSize(5);
        bean.setMaxPoolSize(50);
        bean.setQueueCapacity(10);
        bean.setThreadNamePrefix("cmsadmin-async-");
        return bean;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> c : converters) {
            if (c instanceof MappingJackson2HttpMessageConverter converter) {
                List<MediaType> list = new ArrayList<>();
                list.add(MediaType.TEXT_PLAIN);
                list.add(MediaType.APPLICATION_JSON);
                converter.setSupportedMediaTypes(list);
                SimpleModule module = new SimpleModule();
                module.addSerializer(Long.class, ToStringSerializer.instance);
                module.addSerializer(Long.TYPE, ToStringSerializer.instance);
                converter.getObjectMapper().registerModule(module);
            }
        }
    }
}
