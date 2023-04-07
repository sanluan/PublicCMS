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
import org.hibernate.validator.HibernateValidator;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.github.pagehelper.PageInterceptor;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.search.MultiTokenFilterFactory;
import com.publiccms.common.search.MultiTokenizerFactory;
import com.publiccms.common.tools.AnalyzerDictUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.IdWorker;
import com.publiccms.logic.component.site.DirectiveComponent;
import com.publiccms.logic.component.site.MenuMessageComponent;
import com.publiccms.logic.component.site.SiteComponent;

import config.initializer.InitializationInitializer;
import freemarker.cache.FileTemplateLoader;
import jakarta.validation.ValidatorFactory;

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
public class ApplicationConfig implements EnvironmentAware{
    private Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }

    /**
     * 序列生成器
     *
     * @return idWorker
     * @throws PropertyVetoException
     */
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(Long.valueOf(env.getProperty("cms.workerId")));
    }

    /**
     * 数据源
     *
     * @return datasource
     */
    @Bean
    public DataSource dataSource() {
        CmsDataSource bean = new CmsDataSource(getDirPath(CmsDataSource.DATABASE_CONFIG_FILENAME));
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
        return new HibernateTransactionManager(sessionFactory);
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
        bean.setPlugins(new PageInterceptor());
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
     * @throws IOException
     */
    @Bean
    public FactoryBean<SessionFactory> hibernateSessionFactory(DataSource dataSource) throws IOException {
        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan("com.publiccms.entities");
        Properties properties = PropertiesLoaderUtils.loadAllProperties(env.getProperty("cms.hibernate.configFilePath"));
        String cacheConfigUri = "hibernate.javax.cache.uri";
        if (properties.containsKey(cacheConfigUri)) {
            properties.setProperty(cacheConfigUri, getClass().getResource(properties.getProperty(cacheConfigUri)).toString());
        }
        properties.setProperty("hibernate.search.backend.directory.root", getDirPath("/indexes/"));
        MultiTokenizerFactory.init(env.getProperty("cms.tokenizerFactory"),
                getMap(env.getProperty("cms.tokenizerFactory.parameters")));

        MultiTokenFilterFactory.init(env.getProperty("cms.tokenFilterFactory"),
                getMap(env.getProperty("cms.tokenFilterFactory.parameters")));
        bean.setHibernateProperties(properties);
        return bean;
    }

    /**
     * 验证工厂
     *
     * @return cache factory
     */
    @Bean
    public ValidatorFactory validatorFactoryBean() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setProviderClass(HibernateValidator.class);
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
        return new CacheEntityFactory(env.getProperty("cms.cache.configFilePath"));
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
        bean.setBasenames(StringUtils.split(env.getProperty("cms.language"), Constants.COMMA));
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
        bean.setRootPath(getDirPath(Constants.BLANK));
        bean.setMasterSiteIds(env.getProperty("cms.masterSiteIds"));
        bean.setDefaultSiteId(Short.parseShort(env.getProperty("cms.defaultSiteId")));
        if ("hmmChinese".equalsIgnoreCase(env.getProperty("cms.tokenizerFactory"))) {
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
        bean.setPreTemplateLoaders(new FileTemplateLoader(new File(getDirPath("/template/custom/"))));
        bean.setTemplateLoaderPath("classpath:/templates/");
        Properties properties = PropertiesLoaderUtils.loadAllProperties(env.getProperty("cms.freemarker.configFilePath"));
        if (CommonUtils.notEmpty(env.getProperty("cms.defaultLocale"))
                && !"auto".equalsIgnoreCase(env.getProperty("cms.defaultLocale"))) {
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
     */
    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    /**
     * @param property
     * @return the map
     */
    private Map<String, String> getMap(String property) {
        Map<String, String> parametersMap = new HashMap<>();
        if (CommonUtils.notEmpty(property)) {
            String[] parameters = StringUtils.split(property, Constants.COMMA);
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
        File dir = new File(CommonUtils.joinString(CommonConstants.CMS_FILEPATH, path));
        dir.mkdirs();
        return dir.getAbsolutePath();
    }
}