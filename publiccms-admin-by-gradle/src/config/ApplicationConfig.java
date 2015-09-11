package config;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.mchange.v2.c3p0.DriverManagerDataSource;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.MailComponent;
import com.sanluan.common.datasource.MultiDataSource;
import com.sanluan.common.handler.FreeMarkerExtendHandler;

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

	@Bean
	public DataSource dataSource() {
		MultiDataSource dataSource = new MultiDataSource();
		dataSource.setTargetDataSources(new HashMap<Object, Object>() {
			private static final long serialVersionUID = 1L;
			{
				DriverManagerDataSource database1 = new DriverManagerDataSource();
				database1.setDriverClass(env.getProperty("jdbc.driverClassName"));
				database1.setJdbcUrl(env.getProperty("jdbc.url"));
				database1.setUser(env.getProperty("jdbc.username"));
				database1.setPassword(env.getProperty("jdbc.password"));
				put("database1", database1);
			}
		});
		return dataSource;
	}

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
		hibernateProperties.setProperty("hibernate.current_session_context_class",
				env.getProperty("hibernate.current_session_context_class"));
		hibernateProperties.setProperty("hibernate.cache.provider_configuration_file_resource_path",
				env.getProperty("hibernate.cache.provider_configuration_file_resource_path"));
		hibernateProperties.setProperty("hibernate.search.default.directory_provider",
				env.getProperty("hibernate.search.default.directory_provider"));
		hibernateProperties.setProperty("hibernate.search.default.indexBase", getDataFilePath() + "/indexes");

		bean.setHibernateProperties(hibernateProperties);
		return bean;
	}

	@Bean
	public HibernateTransactionManager hibernateTransactionManager() {
		HibernateTransactionManager bean = new HibernateTransactionManager();
		bean.setSessionFactory(sessionFactory);
		return bean;
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
		bean.setBasename("config.language.message");
		bean.setCacheSeconds(3000);
		return bean;
	}

	@Bean
	public MailComponent mailComponent() {
		MailComponent bean = new MailComponent();
		bean.setFromAddress(env.getProperty("mail.smtp.username"));
		return bean;
	}

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

	@Bean
	public FileComponent fileComponent() {
		FileComponent bean = new FileComponent();
		bean.setTemplateLoaderPath(env.getProperty("file.templateLoaderPath"));
		bean.setDataFilePath(getDataFilePath() + "/pages");
		bean.setStaticFileDirectory(env.getProperty("file.staticFileDirectory"));
		bean.setUploadFilePath(env.getProperty("file.uploadFilePath"));
		bean.setBasePath(basePath);
		bean.setSitePath(env.getProperty("site.domain"));
		bean.setCmsPath(env.getProperty("site.cmsPath"));
		bean.setUploadPath(env.getProperty("site.upload"));
		return bean;
	}

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer bean = new FreeMarkerConfigurer();
		bean.setTemplateLoaderPath(env.getProperty("freemarker.templateLoaderPath"));

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

	@Bean
	public FreeMarkerExtendHandler freeMarkerExtendHandler() {
		FreeMarkerExtendHandler bean = new FreeMarkerExtendHandler();
		bean.setDirectiveBasePackage(env.getProperty("freeMarkerExtendHandler.directiveBasePackage"));
		bean.setDirectivePrefix(env.getProperty("freeMarkerExtendHandler.directivePrefix"));
		bean.setDirectiveRemoveRegex(env.getProperty("freeMarkerExtendHandler.directiveRemoveRegex"));
		bean.setMethodBasePackage(env.getProperty("freeMarkerExtendHandler.methodBasePackage"));
		bean.setMethodRemoveRegex(env.getProperty("freeMarkerExtendHandler.methodRemoveRegex"));
		return bean;
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver bean = new CommonsMultipartResolver();
		bean.setDefaultEncoding("utf-8");
		bean.setMaxUploadSize(102400000);
		return bean;
	}

	@Bean
	public SchedulerFactoryBean scheduler() {
		SchedulerFactoryBean bean = new SchedulerFactoryBean();
		Properties quartzProperties = new Properties();
		quartzProperties.setProperty("org.quartz.threadPool.threadCount", "3");
		bean.setQuartzProperties(quartzProperties);
		return bean;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter();
		return bean;
	}

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