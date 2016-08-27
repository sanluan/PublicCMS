package config;

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

import com.publiccms.common.interceptor.admin.AdminContextInterceptor;
import com.publiccms.common.view.admin.AdminFreeMarkerView;

/**
 * 
 * AdminConfig AdminServlet配置类
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.publiccms.views.controller.admin", useDefaultFilters = false, includeFilters = {
		@ComponentScan.Filter(value = { Controller.class }) })
public class AdminConfig extends WebMvcConfigurerAdapter {
	/**
	 * 视图层解析器
	 * 
	 * @return
	 */
	@Bean
	public ViewResolver jspViewResolver() {
		return new InternalResourceViewResolver() {
			{
				setOrder(1);
				setPrefix("/WEB-INF/template/");
				setSuffix(".jsp");
				setContentType("text/html;charset=UTF-8");
			}
		};
	}

	/**
	 * 视图层解析器
	 * 
	 * @return
	 */
	@Bean
	public ViewResolver viewResolver() {
		return new FreeMarkerViewResolver() {
			{
				setOrder(0);
				setViewClass(AdminFreeMarkerView.class);
				setPrefix("/template/");
				setSuffix(".html");
				setContentType("text/html;charset=UTF-8");
			}
		};
	}

	/**
	 * 拦截器
	 * 
	 * @return
	 */
	@Bean
	public AdminContextInterceptor initializingInterceptor() {
		return new AdminContextInterceptor() {
			{
				setLoginUrl("/login.html");
				setUnauthorizedUrl("/common/unauthorizedUrl.html");
				setLoginJsonUrl("/common/ajaxTimeout.html");
				setNeedNotLoginUrls(new String[] { "/logout", "/common/", "/login" });
				setNeedNotAuthorizedUrls(new String[] { "/index", "/main", "/menus" });
			}
		};
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(initializingInterceptor());
	}
}
