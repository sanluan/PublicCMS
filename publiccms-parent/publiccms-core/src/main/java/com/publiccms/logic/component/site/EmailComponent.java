package com.publiccms.logic.component.site;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigDataComponent;

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
    public static final String CONFIG_PERSONAL = "personal";
    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CommonUtils.joinString(CONFIGPREFIX, CONFIG_CODE);
    private final Log log = LogFactory.getLog(getClass());

    @Resource
    protected ConfigDataComponent configDataComponent;

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
        return send(siteId, new String[] { toAddress }, null, null, title, content, false, null, null);
    }

    /**
     * @param siteId
     * @param toAddress
     * @param cc
     * @param bcc
     * @param title
     * @param content
     * @return whether to send successfully
     * @throws MessagingException
     */
    public boolean send(short siteId, String[] toAddress, String[] cc, String[] bcc, String title, String content)
            throws MessagingException {
        return send(siteId, toAddress, cc, bcc, title, content, false, null, null);
    }

    /**
     * @param siteId
     * @param toAddress
     * @param cc
     * @param bcc
     * @param title
     * @param content
     * @param fileNames
     * @param files
     * @return whether to send successfully
     * @throws MessagingException
     */
    public boolean send(short siteId, String[] toAddress, String[] cc, String[] bcc, String title, String content,
            String[] fileNames, File[] files) throws MessagingException {
        return send(siteId, toAddress, cc, bcc, title, content, false, fileNames, files);
    }

    /**
     * @param siteId
     * @param toAddress
     * @param title
     * @param html
     * @return whether to send successfully
     * @throws MessagingException
     */
    public boolean sendHtml(short siteId, String toAddress, String title, String html) throws MessagingException {
        return send(siteId, new String[] { toAddress }, null, null, title, html, true, null, null);
    }

    /**
     * @param siteId
     * @param toAddress
     * @param cc
     * @param bcc
     * @param title
     * @param html
     * @return whether to send successfully
     * @throws MessagingException
     */
    public boolean sendHtml(short siteId, String[] toAddress, String[] cc, String[] bcc, String title, String html)
            throws MessagingException {
        return send(siteId, toAddress, cc, bcc, title, html, true, null, null);
    }

    /**
     * @param siteId
     * @param toAddress
     * @param cc
     * @param bcc
     * @param title
     * @param html
     * @param fileNames
     * @param files
     * @return whether to send successfully
     * @throws MessagingException
     */
    public boolean sendHtml(short siteId, String[] toAddress, String[] cc, String[] bcc, String title, String html,
            String[] fileNames, File[] files) throws MessagingException {
        return send(siteId, toAddress, cc, bcc, title, html, true, fileNames, files);
    }

    private boolean send(short siteId, String[] toAddress, String[] cc, String[] bcc, String title, String content,
            boolean isHtml, String[] fileNames, File[] files) throws MessagingException {
        Map<String, String> config = configDataComponent.getConfigData(siteId, CONFIG_CODE);
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_FROMADDRESS))) {
            JavaMailSender mailSender = getMailSender(siteId, config);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, Constants.DEFAULT_CHARSET_NAME);
            messageHelper.setTo(toAddress);
            if (CommonUtils.notEmpty(cc)) {
                messageHelper.setCc(cc);
            }
            if (CommonUtils.notEmpty(bcc)) {
                messageHelper.setBcc(bcc);
            }
            try {
                messageHelper.setFrom(config.get(CONFIG_FROMADDRESS), config.get(CONFIG_PERSONAL));
            } catch (UnsupportedEncodingException e) {
                messageHelper.setFrom(config.get(CONFIG_FROMADDRESS));
            }
            if (null != title) {
                title = title.replaceAll("\r|\n", Constants.BLANK);
            }
            messageHelper.setSubject(title);
            messageHelper.setText(content, isHtml);
            if (null != files) {
                int i = 0;
                messageHelper.setEncodeFilenames(true);
                for (File file : files) {
                    if (file.exists() && file.isFile()) {
                        messageHelper.addAttachment((null != fileNames && fileNames.length > i) ? fileNames[i] : file.getName(),
                                file);
                    }
                    i++;
                }
            }
            log.info(new StringBuilder(config.get(CONFIG_FROMADDRESS)).append(" send a email to ")
                    .append(StringUtils.join(toAddress, Constants.COMMA)).append(" title [").append(title).append("]")
                    .toString());
            pool.execute(new SendTask(mailSender, message));
            return true;
        }
        return false;
    }

    @Override
    public String getCode(short siteId, boolean showAll) {
        return CONFIG_CODE;
    }

    @Override
    public String getCodeDescription(Locale locale) {
        return getMessage(locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_DEFAULTENCODING, INPUTTYPE_TEXT, true,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_DEFAULTENCODING)),
                null, Constants.DEFAULT_CHARSET_NAME));
        extendFieldList.add(new SysExtendField(CONFIG_HOST, INPUTTYPE_TEXT, true,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_HOST)), null,
                null));
        extendFieldList.add(new SysExtendField(CONFIG_PORT, INPUTTYPE_NUMBER, true,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PORT)),
                null, String.valueOf(25)));
        extendFieldList.add(new SysExtendField(CONFIG_USERNAME, INPUTTYPE_TEXT, true,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_USERNAME)), null,
                null));
        extendFieldList.add(new SysExtendField(CONFIG_PASSWORD, INPUTTYPE_PASSWORD, true,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PASSWORD)), null,
                null));
        extendFieldList.add(new SysExtendField(CONFIG_TIMEOUT, INPUTTYPE_NUMBER, true,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_TIMEOUT)),
                null, String.valueOf(3000)));
        extendFieldList.add(new SysExtendField(CONFIG_AUTH, INPUTTYPE_BOOLEAN, true,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_AUTH)), null,
                "true"));
        extendFieldList.add(new SysExtendField(CONFIG_SSL, INPUTTYPE_BOOLEAN, true,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_SSL)), null,
                "true"));
        extendFieldList.add(new SysExtendField(CONFIG_FROMADDRESS, INPUTTYPE_EMAIL, true,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_FROMADDRESS)),
                null, null));
        extendFieldList.add(new SysExtendField(CONFIG_PERSONAL, INPUTTYPE_EMAIL, true,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PERSONAL)), null,
                null));
        return extendFieldList;
    }

    @Override
    public void clear(short siteId) {
        cache.remove(siteId);
    }

    @Override
    public void clear() {
        cache.clear(false);
    }

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @Resource
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        cache = cacheEntityFactory.createCacheEntity(CONFIG_CODE, CacheEntityFactory.MEMORY_CACHE_ENTITY);
    }

    @PreDestroy
    public void destroy() {
        pool.shutdown();
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
