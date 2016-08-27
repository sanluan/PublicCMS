package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * WebConfig WebServlet配置类
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.publiccms.views.controller.api", useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(value = { Controller.class }) })
public class ApiConfig extends WebMvcConfigurerAdapter {
}
