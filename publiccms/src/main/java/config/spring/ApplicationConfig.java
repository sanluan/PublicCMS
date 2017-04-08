package config.spring;

import static config.initializer.InitializationInitializer.CMS_CONFIG_FILE;
import static java.lang.Integer.parseInt;
import static org.springframework.core.io.support.PropertiesLoaderUtils.loadAllProperties;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.publiccms.common.analyzer.MultiTokenizerFactory;
import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.datasource.CmsDataSource;
import com.publiccms.logic.component.site.DirectiveComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.sanluan.common.base.Base;
import com.sanluan.common.cache.CacheEntityFactory;

/**
 * <h1>ApplicationConfig</h1>
 * <p>
 * Spring配置类
 * </p>
 * <p>
 * Spring Config Class
 * </p>
 *
 */
@Configuration
@ComponentScan(basePackages = "com.publiccms", excludeFilters = { @ComponentScan.Filter(value = { Controller.class }) })
@MapperScan(basePackages = "com.publiccms.logic.mapper")
@PropertySource({ "classpath:" + CMS_CONFIG_FILE })
@EnableTransactionManagement
@EnableScheduling
public class ApplicationConfig extends Base {
    @Autowired
    private Environment env;
    public static WebApplicationContext webApplicationContext;

    /**
     * <p>
     * 数据源
     * </P>
     * <p>
     * data source
     * </p>
     *
     * @return
     * @throws PropertyVetoException
     */
    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        CmsDataSource bean = new CmsDataSource(env.getProperty("cms.database.configFilePath"));
        return bean;
    }

    /**
     * <p>
     * Hibernate 事务管理
     * </p>
     * <p>
     * Hibernate Transaction Manager
     * </p>
     *
     * @return
     */
    @Bean
    public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager bean = new HibernateTransactionManager();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }

    /**
     * <p>
     * Mybatis 会话工厂
     * </p>
     * <p>
     * Mybatis Session Factory
     * </p>
     * 
     * @return
     * @throws IOException
     */
    @Bean
    public SqlSessionFactoryBean mybatisSqlSessionFactoryBean(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(true);
        configuration.setLazyLoadingEnabled(false);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources("classpath*:com/publiccms/logic/mapper/**/*Mapper.xml"));
        bean.setConfiguration(configuration);
        return bean;
    }

    /**
     * <p>
     * Hibernate 会话工厂类
     * </p>
     * <p>
     * Hibernate Session Factory
     * </p>
     *
     * @return
     * @throws PropertyVetoException
     * @throws IOException
     */
    @Bean
    public FactoryBean<SessionFactory> hibernateSessionFactory(DataSource dataSource) throws PropertyVetoException, IOException {
        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan(new String[] { "com.publiccms.entities" });
        MultiTokenizerFactory.setName(env.getProperty("cms.tokenizerFactory"));
        bean.setHibernateProperties(getHibernateConfig(env.getProperty("cms.hibernate.configFilePath")));
        return bean;
    }

    private Properties getHibernateConfig(String configFilePath) throws IOException {
        Properties properties = loadAllProperties(configFilePath);
        properties.setProperty("hibernate.search.default.indexBase", getDirPath("/indexes/"));
        return properties;
    }

    /**
     * <p>
     * 缓存工厂
     * </p>
     * <p>
     * Cache Factory
     * </p>
     * 
     * @return
     * @throws IOException
     */
    @Bean
    public CacheEntityFactory cacheEntityFactory() throws IOException {
        CacheEntityFactory bean = new CacheEntityFactory(env.getProperty("cms.cache.configFilePath"));
        return bean;
    }

    /**
     * <p>
     * 国际化处理
     * </p>
     * <p>
     * Internationalization
     * </p>
     *
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
        bean.setBasenames(new String[] { "language.message", "language.config", "language.operate" });
        bean.setCacheSeconds(300);
        bean.setUseCodeAsDefaultMessage(true);
        return bean;
    }

    /**
     * <p>
     * 指令组件
     * </p>
     * <p>
     * Directive Component
     * </p>
     *
     * @return
     */
    @Bean
    public DirectiveComponent directiveComponent() {
        DirectiveComponent bean = new DirectiveComponent();
        bean.setDirectiveRemoveRegex(env.getProperty("cms.directiveRemoveRegex"));
        bean.setMethodRemoveRegex(env.getProperty("cms.methodRemoveRegex"));
        return bean;
    }

    /**
     * <p>
     * 模板组件
     * </p>
     * <p>
     * Template Component
     * </p>
     *
     * @return
     */
    @Bean
    public TemplateComponent templateComponent() {
        TemplateComponent bean = new TemplateComponent();
        bean.setDirectivePrefix(env.getProperty("cms.directivePrefix"));
        return bean;
    }

    /**
     * <p>
     * 站点组件
     * </p>
     * <p>
     * Site Component
     * </p>
     *
     * @return
     */
    @Bean
    public SiteComponent siteComponent() {
        SiteComponent bean = new SiteComponent();
        bean.setRootPath(getDirPath(""));
        bean.setSiteMasters(env.getProperty("cms.masterSiteIds"));
        bean.setDefaultSiteId(parseInt(env.getProperty("cms.defaultSiteId")));
        return AbstractFreemarkerView.siteComponent = bean;
    }

    /**
     * <p>
     * FreeMarker配置工厂
     * </p>
     * <p>
     * FreeMarker Configuration Factory
     * </p>
     * 
     * @return
     * @throws IOException
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException {
        FreeMarkerConfigurer bean = new FreeMarkerConfigurer();
        bean.setTemplateLoaderPath("classpath:/templates/");
        Properties properties = loadAllProperties(env.getProperty("cms.freemarker.configFilePath"));
        bean.setFreemarkerSettings(properties);
        return bean;
    }

    /**
     * <p>
     * 文件上传解决方案
     * </p>
     * <p>
     * File Upload Resolver
     * </p>
     * 
     * @return
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver bean = new CommonsMultipartResolver();
        bean.setDefaultEncoding(DEFAULT_CHARSET_NAME);
        bean.setMaxUploadSize(Long.parseLong(env.getProperty("cms.multipart.maxUploadSize")));
        return bean;
    }

    /**
     * <p>
     * json、Jsonp消息转换适配器，用于支持RequestBody、ResponseBody
     * </p>
     * <p>
     * Json、Jsonp Message Converter , Support For RequestBody、ResponseBody
     * </p>
     * 
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    private String getDirPath(String path) {
        String filePath = env.getProperty("cms.filePath") + path;
        File dir = new File(filePath);
        dir.mkdirs();
        return dir.getAbsolutePath();
    }
}