package config.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.FullBeanNameGenerator;
import com.publiccms.interceptor.SiteInterceptor;

import jakarta.annotation.Resource;

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
    @Resource
    private SiteInterceptor siteInterceptor;

    /**
     * json、Jsonp消息转换适配器，用于支持RequestBody、ResponseBody
     *
     * @return json、jsonp message converter , support for
     *         requestbody、responsebody
     */
    @Bean
    public JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter() {
        JacksonJsonHttpMessageConverter bean = new JacksonJsonHttpMessageConverter(Constants.builder);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.TEXT_PLAIN);
        bean.setSupportedMediaTypes(list);
        return bean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(siteInterceptor);
    }
}
