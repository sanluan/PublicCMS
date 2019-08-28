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
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;

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
    public static final String CONFIG_SSL = "ssl";
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
    public JavaMailSender getMailSender(short siteId, Map<String, String> config) {
        JavaMailSenderImpl javaMailSender = cache.get(siteId);
        if (null == javaMailSender) {
            synchronized (cache) {
                javaMailSender = cache.get(siteId);
                if (null == javaMailSender) {
                    javaMailSender = new JavaMailSenderImpl();
                    javaMailSender.setDefaultEncoding(config.get(CONFIG_DEFAULTENCODING));
                    javaMailSender.setHost(config.get(CONFIG_HOST));
                    javaMailSender.setPort(Integer.parseInt(config.get(CONFIG_PORT)));
                    javaMailSender.setUsername(config.get(CONFIG_USERNAME));
                    javaMailSender.setPassword(config.get(CONFIG_PASSWORD));
                    Properties properties = new Properties();
                    if ("true".equalsIgnoreCase(config.get(CONFIG_SSL))) {
                        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    }
                    properties.setProperty("mail.smtp.auth", config.get(CONFIG_AUTH));
                    properties.setProperty("mail.smtp.timeout", config.get(CONFIG_TIMEOUT));
                    javaMailSender.setJavaMailProperties(properties);
                    cache.put(siteId, javaMailSender);
                }
            }
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
            if (null != title) {
                title = title.replaceAll("\r|\n", CommonConstants.BLANK);
            }
            messageHelper.setSubject(title);
            messageHelper.setText(content, isHtml);
            pool.execute(new SendTask(mailSender, message));
            return true;
        }
        return false;
    }

    @Override
    public String getCode(SysSite site, boolean showAll) {
        return CONFIG_CODE;
    }

    @Override
    public String getCodeDescription(Locale locale) {
        return getMessage(locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_DEFAULTENCODING, INPUTTYPE_TEXT, true, CONFIG_DEFAULTENCODING,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_DEFAULTENCODING),
                CommonConstants.DEFAULT_CHARSET_NAME));
        extendFieldList.add(new SysExtendField(CONFIG_HOST, INPUTTYPE_TEXT, true, CONFIG_HOST,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_HOST), null));
        extendFieldList.add(new SysExtendField(CONFIG_PORT, INPUTTYPE_NUMBER, true, CONFIG_PORT,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_PORT),
                String.valueOf(25)));
        extendFieldList.add(new SysExtendField(CONFIG_USERNAME, INPUTTYPE_TEXT, true, CONFIG_USERNAME,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_USERNAME), null));
        extendFieldList.add(new SysExtendField(CONFIG_PASSWORD, INPUTTYPE_PASSWORD, true, CONFIG_PASSWORD,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_PASSWORD), null));
        extendFieldList.add(new SysExtendField(CONFIG_TIMEOUT, INPUTTYPE_NUMBER, true, CONFIG_TIMEOUT,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_TIMEOUT),
                String.valueOf(3000)));
        extendFieldList.add(new SysExtendField(CONFIG_AUTH, INPUTTYPE_BOOLEAN, true, CONFIG_AUTH,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_AUTH), null));
        extendFieldList.add(new SysExtendField(CONFIG_SSL, INPUTTYPE_BOOLEAN, true, CONFIG_SSL,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_SSL), null));
        extendFieldList.add(new SysExtendField(CONFIG_FROMADDRESS, INPUTTYPE_EMAIL, true, CONFIG_FROMADDRESS,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_FROMADDRESS), null));
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
        while (i < 2) {
            try {
                mailSender.send(message);
                break;
            } catch (Exception e) {
                log.error(e.getMessage());
                i++;
            }
        }
    }
}
