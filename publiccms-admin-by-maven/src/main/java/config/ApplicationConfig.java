package config;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.beans.PropertyVetoException;
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
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.publiccms.logic.component.CacheComponent;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.MailComponent;
import com.sanluan.common.datasource.MultiDataSource;
import com.sanluan.common.handler.FreeMarkerExtendHandler;

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
public class ApplicationConfig {
    @Autowired
    private Environment env;
    @Autowired
    private SessionFactory sessionFactory;
    private String dataFilePath;
    public static String basePath;

    /**
     * 数据源
     * 
     * @return
     */
    @Bean
    public DataSource dataSource() {
        MultiDataSource dataSource = new MultiDataSource();

        dataSource.setTargetDataSources(new HashMap<Object, Object>() {
            private static final long serialVersionUID = 1L;
            {
                try {
                    ComboPooledDataSource database1 = new ComboPooledDataSource();
                    database1.setDriverClass(env.getProperty("jdbc.driverClassName"));
                    database1.setJdbcUrl(env.getProperty("jdbc.url"));
                    database1.setUser(env.getProperty("jdbc.username"));
                    database1.setPassword(env.getProperty("jdbc.password"));
                    database1.setAutoCommitOnClose(Boolean.parseBoolean(env.getProperty("cpool.autoCommitOnClose")));
                    database1.setCheckoutTimeout(Integer.parseInt(env.getProperty("cpool.checkoutTimeout")));
                    database1.setInitialPoolSize(Integer.parseInt(env.getProperty("cpool.minPoolSize")));
                    database1.setMinPoolSize(Integer.parseInt(env.getProperty("cpool.minPoolSize")));
                    database1.setMaxPoolSize(Integer.parseInt(env.getProperty("cpool.maxPoolSize")));
                    database1.setMaxIdleTime(Integer.parseInt(env.getProperty("cpool.maxIdleTime")));
                    database1.setAcquireIncrement(Integer.parseInt(env.getProperty("cpool.acquireIncrement")));
                    database1.setMaxIdleTimeExcessConnections(Integer.parseInt(env
                            .getProperty("cpool.maxIdleTimeExcessConnections")));
                    put("database1", database1);
                } catch (PropertyVetoException e) {
                }
            }
        });

        return dataSource;
    }

    /**
     * 持久层会话工厂类
     * 
     * @return
     */
    @Bean
    public FactoryBean<SessionFactory> sessionFactory() {
        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        bean.setDataSource(dataSource());
        bean.setPackagesToScan(new String[] { "com.publiccms.entities" });

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        hibernateProperties.setProperty("hibernate.query.substitutions", env.getProperty("hibernate.query.substitutions"));
        hibernateProperties.setProperty("hibernate.jdbc.fetch_size", env.getProperty("hibernate.jdbc.fetch_size"));
        hibernateProperties.setProperty("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
        hibernateProperties.setProperty("hibernate.cache.region.factory_class",
                env.getProperty("hibernate.cache.region.factory_class"));
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache",
                env.getProperty("hibernate.cache.use_second_level_cache"));
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
        hibernateProperties.setProperty("hibernate.transaction.coordinator_class",
                env.getProperty("hibernate.transaction.coordinator_class"));
        hibernateProperties.setProperty("hibernate.cache.provider_configuration_file_resource_path",
                env.getProperty("hibernate.cache.provider_configuration_file_resource_path"));
        hibernateProperties.setProperty("hibernate.search.default.directory_provider",
                env.getProperty("hibernate.search.default.directory_provider"));
        hibernateProperties.setProperty("hibernate.search.default.indexBase", getDataFilePath() + "/indexes");

        bean.setHibernateProperties(hibernateProperties);
        return bean;
    }

    /**
     * Hibernate事务管理
     * 
     * @return
     */
    @Bean
    public HibernateTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager bean = new HibernateTransactionManager();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }

    /**
     * 国际化处理
     * 
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
        bean.setBasename("config.language.message");
        bean.setCacheSeconds(3000);
        return bean;
    }

    /**
     * 邮件发送组件
     * 
     * @return
     */
    @Bean
    public MailComponent mailComponent() {
        MailComponent bean = new MailComponent();
        bean.setFromAddress(env.getProperty("mail.smtp.username"));
        return bean;
    }

    /**
     * 邮件发送服务实现
     * 
     * @return
     */
    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl bean = new JavaMailSenderImpl();
        bean.setDefaultEncoding(env.getProperty("mail.smtp.defaultEncoding"));
        bean.setPort(env.getProperty("mail.smtp.port", Integer.class));
        bean.setHost(env.getProperty("mail.smtp.host"));
        bean.setUsername(env.getProperty("mail.smtp.username"));
        bean.setPassword(env.getProperty("mail.smtp.password"));

        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        javaMailProperties.setProperty("mail.smtp.timeout", env.getProperty("mail.smtp.timeout"));
        bean.setJavaMailProperties(javaMailProperties);
        return bean;
    }

    /**
     * 文件操作组件
     * 
     * @return
     */
    @Bean
    public FileComponent fileComponent() {
        FileComponent bean = new FileComponent();
        bean.setBasePath(basePath);
        bean.setTemplateLoaderPath(env.getProperty("file.templateLoaderPath"));
        bean.setDataFilePath(getDataFilePath() + "/pages");
        bean.setStaticFileDirectory(env.getProperty("file.staticFileDirectory"));
        bean.setUploadFilePath(env.getProperty("file.uploadFilePath"));
        bean.setSitePath(env.getProperty("site.domain"));
        bean.setCmsPath(env.getProperty("site.cmsPath"));
        bean.setUploadPath(env.getProperty("site.upload"));
        return bean;
    }

    /**
     * FreeMarker配置工厂
     * 
     * @return
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer bean = new FreeMarkerConfigurer();
        bean.setTemplateLoaderPath(CacheComponent.TEMPLATE_BASE_PATH);

        Properties freemarkerSettings = new Properties();
        freemarkerSettings.setProperty("template_update_delay", env.getProperty("freemarkerSettings.template_update_delay"));
        freemarkerSettings.setProperty("defaultEncoding", env.getProperty("freemarkerSettings.defaultEncoding"));
        freemarkerSettings.setProperty("url_escaping_charset", env.getProperty("freemarkerSettings.url_escaping_charset"));
        freemarkerSettings.setProperty("locale", env.getProperty("freemarkerSettings.locale"));
        freemarkerSettings.setProperty("boolean_format", env.getProperty("freemarkerSettings.boolean_format"));
        freemarkerSettings.setProperty("datetime_format", env.getProperty("freemarkerSettings.datetime_format"));
        freemarkerSettings.setProperty("date_format", env.getProperty("freemarkerSettings.date_format"));
        freemarkerSettings.setProperty("time_format", env.getProperty("freemarkerSettings.time_format"));
        freemarkerSettings.setProperty("number_format", env.getProperty("freemarkerSettings.number_format"));
        freemarkerSettings.setProperty("auto_import", env.getProperty("freemarkerSettings.auto_import"));
        freemarkerSettings.setProperty("auto_include", env.getProperty("freemarkerSettings.auto_include"));
        freemarkerSettings.setProperty("template_exception_handler",
                env.getProperty("freemarkerSettings.template_exception_handler"));
        bean.setFreemarkerSettings(freemarkerSettings);
        return bean;
    }

    /**
     * FreeMarker指令、方法扩展处理器
     * 
     * @return
     */
    @Bean
    public FreeMarkerExtendHandler freeMarkerExtendHandler() {
        FreeMarkerExtendHandler bean = new FreeMarkerExtendHandler();
        bean.setDirectivePrefix(env.getProperty("freeMarkerExtendHandler.directivePrefix"));
        bean.setDirectiveRemoveRegex(env.getProperty("freeMarkerExtendHandler.directiveRemoveRegex"));
        bean.setMethodRemoveRegex(env.getProperty("freeMarkerExtendHandler.methodRemoveRegex"));
        return bean;
    }

    /**
     * 附件Multipart解决方案
     * 
     * @return
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver bean = new CommonsMultipartResolver();
        bean.setDefaultEncoding("utf-8");
        bean.setMaxUploadSize(102400000);
        return bean;
    }

    /**
     * 任务计划工厂类配置
     * 
     * @return
     */
    @Bean
    public SchedulerFactoryBean scheduler() {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        Properties quartzProperties = new Properties();
        quartzProperties.setProperty("org.quartz.threadPool.threadCount", "3");
        bean.setQuartzProperties(quartzProperties);
        return bean;
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

    /**
     * 数据文件存储路径
     * 
     * @return
     */
    public String getDataFilePath() {
        if (null == dataFilePath) {
            dataFilePath = env.getProperty("file.dataFilePath");
            if (isBlank(dataFilePath)) {
                dataFilePath = basePath + FileComponent.DEFAULT_DATA_FILE_PATH;
            }
        }
        return dataFilePath;
    }
}