package com.publiccms.logic.component.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.views.pojo.entities.ExtendField;

/**
 * 
 * MailComponent 邮件发送组件
 *
 */
@Component
public class EmailComponent implements SiteCache, Config {

    /**
     * 
     */
    public static final String CONFIG_CODE = "email";
    /**
     * 
     */
    public static final String CONFIG_DEFAULTENCODING = "defaultEncoding";
    /**
     * 
     */
    public static final String CONFIG_HOST = "host";
    /**
     * 
     */
    public static final String CONFIG_PORT = "port";
    /**
     * 
     */
    public static final String CONFIG_USERNAME = "username";
    /**
     * 
     */
    public static final String CONFIG_PASSWORD = "password";
    /**
     * 
     */
    public static final String CONFIG_TIMEOUT = "timeout";
    /**
     * 
     */
    public static final String CONFIG_AUTH = "auth";
    /**
     * 
     */
    public static final String CONFIG_FROMADDRESS = "fromAddress";
    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE;

    @Autowired
    private ConfigComponent configComponent;

    private CacheEntity<Short, JavaMailSenderImpl> cache;

    private static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * @param siteId
     * @param config
     * @return mail sender
     */
    public synchronized JavaMailSender getMailSender(short siteId, Map<String, String> config) {
        JavaMailSenderImpl javaMailSender = cache.get(siteId);
        if (null == javaMailSender) {
            javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setDefaultEncoding(config.get(CONFIG_DEFAULTENCODING));
            javaMailSender.setHost(config.get(CONFIG_HOST));
            javaMailSender.setPort(Integer.parseInt(config.get(CONFIG_PORT)));
            javaMailSender.setUsername(config.get(CONFIG_USERNAME));
            javaMailSender.setPassword(config.get(CONFIG_PASSWORD));
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth", config.get(CONFIG_AUTH));
            properties.setProperty("mail.smtp.timeout", config.get(CONFIG_TIMEOUT));
            javaMailSender.setJavaMailProperties(properties);
            cache.put(siteId, javaMailSender);
        }
        return javaMailSender;
    }

    /**
     * @param siteId
     * @param toAddress
     * @param title
     * @param content
     * @return whether to send successfully
     * @throws MessagingException
     */
    public boolean send(short siteId, String toAddress, String title, String content) throws MessagingException {
        return send(siteId, toAddress, title, content, false);
    }

    /**
     * @param siteId
     * @param toAddress
     * @param title
     * @param html
     * @return
     * @throws MessagingException
     */
    public boolean sendHtml(short siteId, String toAddress, String title, String html) throws MessagingException {
        return send(siteId, toAddress, title, html, true);
    }

    /**
     * @param toAddress
     * @param fromAddress
     * @param title
     * @param content
     * @param isHtml
     * @return whether to send successfully
     * @throws MessagingException
     */
    private boolean send(short siteId, String toAddress, String title, String content, boolean isHtml) throws MessagingException {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE);
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_FROMADDRESS))) {
            JavaMailSender mailSender = getMailSender(siteId, config);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, CommonConstants.DEFAULT_CHARSET_NAME);
            messageHelper.setTo(toAddress);
            messageHelper.setFrom(config.get(CONFIG_FROMADDRESS));
            messageHelper.setSubject(title);
            messageHelper.setText(content, isHtml);
            pool.execute(new SendTask(mailSender, message));
            return true;
        }
        return false;
    }

    @Override
    public String getCode(SysSite site) {
        return CONFIG_CODE;
    }

    @Override
    public String getCodeDescription(SysSite site, Locale locale) {
        return LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<ExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new ExtendField(CONFIG_DEFAULTENCODING, INPUTTYPE_TEXT, true,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_DEFAULTENCODING),
                null, CommonConstants.DEFAULT_CHARSET_NAME));
        extendFieldList.add(
                new ExtendField(CONFIG_HOST, INPUTTYPE_TEXT, true, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                        locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_HOST), null, null));
        extendFieldList.add(
                new ExtendField(CONFIG_PORT, INPUTTYPE_NUMBER, true, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                        locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_PORT), null, String.valueOf(25)));
        extendFieldList.add(new ExtendField(CONFIG_USERNAME, INPUTTYPE_TEXT, true,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_USERNAME),
                null, null));
        extendFieldList.add(new ExtendField(CONFIG_PASSWORD, INPUTTYPE_PASSWORD, true,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_PASSWORD),
                null, null));
        extendFieldList
                .add(new ExtendField(
                        CONFIG_TIMEOUT, INPUTTYPE_NUMBER, true, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                                locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_TIMEOUT),
                        null, String.valueOf(3000)));
        extendFieldList.add(new ExtendField(CONFIG_AUTH, INPUTTYPE_BOOLEAN, true,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_AUTH),
                null, null));
        extendFieldList.add(new ExtendField(CONFIG_FROMADDRESS, INPUTTYPE_EMAIL, true,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_FROMADDRESS),
                null, null));
        return extendFieldList;
    }

    @Override
    public void clear(short siteId) {
        cache.remove(siteId);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        cache = cacheEntityFactory.createCacheEntity(CONFIG_CODE, CacheEntityFactory.MEMORY_CACHE_ENTITY);
    }
}

/**
 * 
 * SendTask 邮件发送线程
 *
 */
class SendTask implements Runnable {
    private JavaMailSender mailSender;
    private MimeMessage message;
    private final Log log = LogFactory.getLog(getClass());

    public SendTask(JavaMailSender mailSender, MimeMessage message) {
        this.message = message;
        this.mailSender = mailSender;
    }

    @Override
    public void run() {
        int i = 0;
        while (i < 3) {
            try {
                mailSender.send(message);
                break;
            } catch (Exception e) {
                i++;
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e1) {
                    log.error(e1.getMessage());
                }
            }
        }
    }
}
