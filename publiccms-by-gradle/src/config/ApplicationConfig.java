package config;

import java.beans.PropertyVetoException;
import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.publiccms.common.view.InitializeFreeMarkerView;
import com.publiccms.logic.component.SiteComponent;
import com.publiccms.logic.component.TemplateComponent;
import com.sanluan.common.base.Base;
import com.sanluan.common.datasource.MultiDataSource;

/**
 * 
 * ApplicationConfig Spring配置类
 *
 */
@Configuration
@ComponentScan(basePackages = "com.publiccms", excludeFilters = { @ComponentScan.Filter(value = { Controller.class }) })
@PropertySource({ "classpath:config/properties/dbconfig.properties", "classpath:config/properties/freemarker.properties",
        "classpath:config/properties/other.properties" })
@EnableTransactionManagement
@EnableScheduling
public class ApplicationConfig extends Base {
    @Autowired
    private Environment env;
    @Autowired
    private SessionFactory sessionFactory;
    public static String basePath;
    public static WebApplicationContext webApplicationContext;

    /**
     * 数据源
     * 
     * @return
     * @throws PropertyVetoException
     */
    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        return new MultiDataSource() {
            {
                setTargetDataSources(new HashMap<Object, Object>() {
                    private static final long serialVersionUID = 1L;
                    {
                        ComboPooledDataSource database = new ComboPooledDataSource();
                        database.setDriverClass(env.getProperty("jdbc.driverClassName"));
                        database.setJdbcUrl(env.getProperty("jdbc.url"));
                        database.setUser(env.getProperty("jdbc.username"));
                        database.setPassword(env.getProperty("jdbc.password"));
                        database.setAutoCommitOnClose(Boolean.parseBoolean(env.getProperty("cpool.autoCommitOnClose")));
                        database.setCheckoutTimeout(Integer.parseInt(env.getProperty("cpool.checkoutTimeout")));
                        database.setInitialPoolSize(Integer.parseInt(env.getProperty("cpool.minPoolSize")));
                        database.setMinPoolSize(Integer.parseInt(env.getProperty("cpool.minPoolSize")));
                        database.setMaxPoolSize(Integer.parseInt(env.getProperty("cpool.maxPoolSize")));
                        database.setMaxIdleTime(Integer.parseInt(env.getProperty("cpool.maxIdleTime")));
                        database.setAcquireIncrement(Integer.parseInt(env.getProperty("cpool.acquireIncrement")));
                        database.setMaxIdleTimeExcessConnections(
                                Integer.parseInt(env.getProperty("cpool.maxIdleTimeExcessConnections")));
                        put("default", database);
                    }
                });
            }
        };
    }

    /**
     * Hibernate事务管理
     * 
     * @return
     */
    @Bean
    public HibernateTransactionManager hibernateTransactionManager() {
        return new HibernateTransactionManager() {
            private static final long serialVersionUID = 1L;
            {
                setSessionFactory(sessionFactory);
            }
        };
    }

    /**
     * 持久层会话工厂类
     * 
     * @return
     * @throws PropertyVetoException
     */
    @Bean
    public FactoryBean<SessionFactory> sessionFactory() throws PropertyVetoException {
        return new LocalSessionFactoryBean() {
            {
                setDataSource(dataSource());
                setPackagesToScan(new String[] { "com.publiccms.entities" });
                setHibernateProperties(new Properties() {
                    private static final long serialVersionUID = 1L;
                    {
                        setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
                        setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
                        setProperty("hibernate.query.substitutions", env.getProperty("hibernate.query.substitutions"));
                        setProperty("hibernate.jdbc.fetch_size", env.getProperty("hibernate.jdbc.fetch_size"));
                        setProperty("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
                        setProperty("hibernate.cache.region.factory_class",
                                env.getProperty("hibernate.cache.region.factory_class"));
                        setProperty("hibernate.cache.use_second_level_cache",
                                env.getProperty("hibernate.cache.use_second_level_cache"));
                        setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
                        setProperty("hibernate.transaction.coordinator_class",
                                env.getProperty("hibernate.transaction.coordinator_class"));
                        setProperty("hibernate.cache.provider_configuration_file_resource_path",
                                env.getProperty("hibernate.cache.provider_configuration_file_resource_path"));
                        setProperty("hibernate.search.default.directory_provider",
                                env.getProperty("hibernate.search.default.directory_provider"));
                        setProperty("hibernate.search.default.indexBase", getDirPath("/indexes/"));
                    }
                });
            }
        };
    }

    /**
     * 国际化处理
     * 
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        return new ResourceBundleMessageSource() {
            {
                setBasenames(new String[] { "config.language.message", "config.language.config", "config.language.plugin" });
                setCacheSeconds(300);
                setUseCodeAsDefaultMessage(true);
            }
        };
    }

    /**
     * 模板操作组件
     * 
     * @return
     */
    @Bean
    public TemplateComponent templateComponent() {
        return new TemplateComponent() {
            {
                setDirectivePrefix(env.getProperty("freeMarkerExtendHandler.directivePrefix"));
                setDirectiveRemoveRegex(env.getProperty("freeMarkerExtendHandler.directiveRemoveRegex"));
                setMethodRemoveRegex(env.getProperty("freeMarkerExtendHandler.methodRemoveRegex"));
            }
        };
    }

    /**
     * 文件操作组件
     * 
     * @return
     */
    @Bean
    public SiteComponent siteComponent() {
        return InitializeFreeMarkerView.siteComponent = new SiteComponent() {
            {
                setRootPath(getDirPath(""));
                setSiteMasters(env.getProperty("site.masterSiteIds"));
                setDefaultSiteId(Integer.parseInt(env.getProperty("site.defaultSiteId")));
            }
        };
    }

    /**
     * FreeMarker配置工厂
     * 
     * @return
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        return new FreeMarkerConfigurer() {
            {
                setTemplateLoaderPath("/WEB-INF/");
                setFreemarkerSettings(new Properties() {
                    private static final long serialVersionUID = 1L;
                    {
                        setProperty("new_builtin_class_resolver",
                                env.getProperty("freemarkerSettings.new_builtin_class_resolver"));
                        setProperty("template_update_delay", env.getProperty("freemarkerSettings.template_update_delay"));
                        setProperty("defaultEncoding", env.getProperty("freemarkerSettings.defaultEncoding"));
                        setProperty("url_escaping_charset", env.getProperty("freemarkerSettings.url_escaping_charset"));
                        setProperty("locale", env.getProperty("freemarkerSettings.locale"));
                        setProperty("boolean_format", env.getProperty("freemarkerSettings.boolean_format"));
                        setProperty("datetime_format", env.getProperty("freemarkerSettings.datetime_format"));
                        setProperty("date_format", env.getProperty("freemarkerSettings.date_format"));
                        setProperty("time_format", env.getProperty("freemarkerSettings.time_format"));
                        setProperty("number_format", env.getProperty("freemarkerSettings.number_format"));
                        setProperty("auto_import", env.getProperty("freemarkerSettings.auto_import"));
                        setProperty("auto_include", env.getProperty("freemarkerSettings.auto_include"));
                        setProperty("template_exception_handler",
                                env.getProperty("freemarkerSettings.template_exception_handler"));
                    }
                });
            }
        };
    }

    /**
     * 附件Multipart解决方案
     * 
     * @return
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver() {
            {
                setDefaultEncoding(DEFAULT_CHARSET);
                setMaxUploadSize(102400000);
            }
        };
    }

    /**
     * 任务计划工厂类配置
     * 
     * @return
     */
    @Bean
    public SchedulerFactoryBean scheduler() {
        return new SchedulerFactoryBean() {
            {
                setQuartzProperties(new Properties() {
                    private static final long serialVersionUID = 1L;
                    {
                        setProperty("org.quartz.threadPool.threadCount", env.getProperty("task.threadCount"));
                    }
                });
            }
        };
    }

    /**
     * json消息转换适配器，用于支持RequestBody、ResponseBody
     * 
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    private String getDirPath(String path) {
        String filePath = env.getProperty("site.filePath", basePath) + path;
        File dir = new File(filePath);
        dir.mkdirs();
        return dir.getAbsolutePath();
    }
}