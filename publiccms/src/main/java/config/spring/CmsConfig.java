package config.spring;

import static org.springframework.scheduling.quartz.SchedulerFactoryBean.PROP_THREAD_COUNT;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import com.publiccms.common.servlet.MultiSiteWebHttpRequestHandler;
import com.publiccms.logic.component.site.SiteComponent;

/**
 * <h1>CmsRootConfig</h1>
 * <p>
 * Cms跟配置类
 * </p>
 * <p>
 * Spring Config Class
 * </p>
 *
 */
@Import(ApplicationConfig.class)
public class CmsConfig {
    @Autowired
    private Environment env;

    /**
     * <p>
     * 资源处理器
     * </p>
     * <p>
     * DefaultServletHttpRequestHandler
     * </p>
     * 
     * @return
     */
    @Bean
    public HttpRequestHandler defaultServlet() {
        DefaultServletHttpRequestHandler bean = new DefaultServletHttpRequestHandler();
        return bean;
    }

    /**
     * <p>
     * 站点静态页面处理器
     * </p>
     * <p>
     * DefaultServletHttpRequestHandler
     * </p>
     * 
     * @return
     */
    @Bean
    public HttpRequestHandler webfileServlet(SiteComponent siteComponent) {
        MultiSiteWebHttpRequestHandler bean = new MultiSiteWebHttpRequestHandler(siteComponent);
        return bean;
    }

    /**
     * <p>
     * 任务计划工厂
     * </p>
     * <p>
     * Task Scheduler Factory
     * </p>
     * 
     * @return
     */
    @Bean
    public SchedulerFactoryBean scheduler() {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty(PROP_THREAD_COUNT, env.getProperty("cms.task.threadCount"));
        bean.setQuartzProperties(properties);
        return bean;
    }
}