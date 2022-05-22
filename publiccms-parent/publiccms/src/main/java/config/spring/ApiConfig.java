package config.spring;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.publiccms.common.handler.FullBeanNameGenerator;
import com.publiccms.common.interceptor.SiteInterceptor;

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
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter();
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.TEXT_PLAIN);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        bean.getObjectMapper().registerModule(module);
        bean.setSupportedMediaTypes(list);
        return bean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(siteInterceptor);
    }
}
