package config.spring;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.cn.smart.hhmm.DictionaryReloader;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.search.MultiTokenFilterFactory;
import com.publiccms.common.search.MultiTokenizerFactory;
import com.publiccms.common.tools.AnalyzerDictUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.site.DirectiveComponent;
import com.publiccms.logic.component.site.MenuMessageComponent;
import com.publiccms.logic.component.site.SiteComponent;

import config.initializer.InitializationInitializer;

/**
 *
 * Spring配置类
 *
 * Spring Configuration Class
 *
 */
@Configuration
@ComponentScan(basePackages = "com.publiccms", excludeFilters = { @ComponentScan.Filter(value = { Controller.class }) })
@MapperScan(basePackages = "com.publiccms.logic.mapper")
@PropertySource({ "classpath:" + CommonConstants.CMS_CONFIG_FILE })
@EnableTransactionManagement
@EnableScheduling
public class ApplicationConfig {

    @Autowired
    private Environment env;

    /**
     * 数据源
     *
     * @return datasource
     * @throws PropertyVetoException
     */
    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        CmsDataSource bean = new CmsDataSource(getDirPath(CommonConstants.BLANK) + CmsDataSource.DATABASE_CONFIG_FILENAME);
        CmsDataSource.initDefaultDataSource();
        return bean;
    }

    /**
     * Hibernate 事务管理
     *
     * @param sessionFactory
     * @return hibernate transaction manager
     */
    @Bean
    public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager bean = new HibernateTransactionManager();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }

    /**
     * Mybatis会话工厂
     *
     * @param dataSource
     * @return mybatis session factory
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
        bean.setMapperLocations(resolver.getResources("classpath*:mapper/**/*Mapper.xml"));
        bean.setConfiguration(configuration);
        return bean;
    }

    /**
     * Hibernate 会话工厂类
     *
     * @param dataSource
     * @return hibernate session factory
     * @throws PropertyVetoException
     * @throws IOException
     */
    @Bean
    public FactoryBean<SessionFactory> hibernateSessionFactory(DataSource dataSource) throws PropertyVetoException, IOException {
        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan("com.publiccms.entities");
        Properties properties = PropertiesLoaderUtils.loadAllProperties(env.getProperty("cms.hibernate.configFilePath"));
        String cacheConfigUri = "hibernate.javax.cache.uri";
        if (properties.containsKey(cacheConfigUri)) {
            properties.setProperty(cacheConfigUri, getClass().getResource(properties.getProperty(cacheConfigUri)).toString());
        }
        properties.setProperty("hibernate.search.default.indexBase", getDirPath("/indexes/"));
        MultiTokenizerFactory.init(env.getProperty("cms.tokenizerFactory"),
                getMap(env.getProperty("cms.tokenizerFactory.parameters")));

        MultiTokenFilterFactory.init(env.getProperty("cms.tokenFilterFactory"),
                getMap(env.getProperty("cms.tokenFilterFactory.parameters")));
        bean.setHibernateProperties(properties);
        return bean;
    }

    /**
     * 缓存工厂
     *
     * @return cache factory
     * @throws IOException
     */
    @Bean
    public CacheEntityFactory cacheEntityFactory() throws IOException {
        CacheEntityFactory bean = new CacheEntityFactory(env.getProperty("cms.cache.configFilePath"));
        return bean;
    }

    /**
     * 国际化处理
     * 
     * @param menuMessageComponent
     *
     * @return message source
     */
    @Bean
    public MessageSource messageSource(MenuMessageComponent menuMessageComponent) {
        ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
        bean.setBasenames(StringUtils.split(env.getProperty("cms.language"), CommonConstants.COMMA_DELIMITED));
        bean.setCacheSeconds(300);
        bean.setUseCodeAsDefaultMessage(true);
        bean.setParentMessageSource(menuMessageComponent);
        return bean;
    }

    /**
     * 指令组件
     *
     * @return directive component
     */
    @Bean
    public DirectiveComponent directiveComponent() {
        DirectiveComponent bean = new DirectiveComponent();
        bean.setDirectiveRemoveRegex(env.getProperty("cms.directiveRemoveRegex"));
        bean.setMethodRemoveRegex(env.getProperty("cms.methodRemoveRegex"));
        bean.setDirectivePrefix(env.getProperty("cms.directivePrefix"));
        return bean;
    }

    /**
     * 站点组件
     *
     * @return site component
     */
    @Bean
    public SiteComponent siteComponent() {
        SiteComponent bean = new SiteComponent();
        bean.setRootPath(getDirPath(CommonConstants.BLANK));
        bean.setMasterSiteIds(env.getProperty("cms.masterSiteIds"));
        bean.setDefaultSiteId(Short.parseShort(env.getProperty("cms.defaultSiteId")));
        if ("hmmchinese".equalsIgnoreCase(env.getProperty("cms.tokenizerFactory"))) {
            String dictDirPath = getDirPath(AnalyzerDictUtils.DIR_DICT);
            File dictDir = new File(dictDirPath);
            if (dictDir.exists() && dictDir.isDirectory()) {
                DictionaryReloader.reload(dictDirPath);// 自定义词库
            }
            bean.setDictEnable(true);
        }
        return bean;
    }

    /**
     * FreeMarker配置工厂
     *
     * @return freemarker configuration factory
     * @throws IOException
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException {
        FreeMarkerConfigurer bean = new FreeMarkerConfigurer();
        bean.setTemplateLoaderPath("classpath:/templates/");
        Properties properties = PropertiesLoaderUtils.loadAllProperties(env.getProperty("cms.freemarker.configFilePath"));
        if (CommonUtils.notEmpty(env.getProperty("cms.defaultLocale"))) {
            properties.put("locale", env.getProperty("cms.defaultLocale"));
        }
        bean.setFreemarkerSettings(properties);
        return bean;
    }

    /**
     *
     * 任务计划工厂
     *
     * @return task scheduler factory
     */
    @Bean
    public SchedulerFactoryBean scheduler() {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty(SchedulerFactoryBean.PROP_THREAD_COUNT, env.getProperty("cms.task.threadCount"));
        bean.setQuartzProperties(properties);
        return bean;
    }

    /**
     * 文件上传解决方案
     *
     * @return file upload resolver
     * @throws IOException
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() throws IOException {
        CommonsMultipartResolver bean = new CommonsMultipartResolver();
        bean.setDefaultEncoding(CommonConstants.DEFAULT_CHARSET_NAME);
        bean.setMaxUploadSize(Long.parseLong(env.getProperty("cms.multipart.maxUploadSize")) * 1024 * 1024);
        bean.setUploadTempDir(new FileSystemResource(getDirPath("/tmp/")));
        return bean;
    }

    /**
     * json、Jsonp消息转换适配器，用于支持RequestBody、ResponseBody
     *
     * @return json、jsonp message converter , support for
     *         requestbody、responsebody
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    /**
     * @param property
     * @return the map
     */
    private Map<String, String> getMap(String property) {
        Map<String, String> parametersMap = new HashMap<>();
        if (CommonUtils.notEmpty(property)) {
            String[] parameters = StringUtils.split(property, CommonConstants.COMMA_DELIMITED);
            for (String parameter : parameters) {
                String[] values = StringUtils.split(parameter, "=", 2);
                if (values.length == 2) {
                    parametersMap.put(values[0], values[1]);
                } else if (values.length == 1) {
                    parametersMap.put(values[0], null);
                }
            }
        }
        return parametersMap;
    }

    /**
     * @param path
     * @return the cms data path
     */
    private String getDirPath(String path) {
        if (null == CommonConstants.CMS_FILEPATH) {
            InitializationInitializer.initFilePath(env.getProperty("cms.filePath"), System.getProperty("user.dir"));
        }
        File dir = new File(CommonConstants.CMS_FILEPATH + path);
        dir.mkdirs();
        return dir.getAbsolutePath();
    }
}