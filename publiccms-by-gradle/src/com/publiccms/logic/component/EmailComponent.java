package com.publiccms.logic.component;

import static com.sanluan.common.tools.LanguagesUtils.getMessage;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;
import static org.apache.commons.logging.LogFactory.getLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Cacheable;
import com.publiccms.common.spi.Configable;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.base.Base;

/**
 * 
 * MailComponent 邮件发送组件
 *
 */
@Component
public class EmailComponent extends Base implements Cacheable, Configable {
    public static final String CONFIG_CODE = "email";
    public static final String CONFIG_SUBCODE = "emailserver";
    public static final String CONFIG_EMAIL_SMTP_DEFAULTENCODING = "defaultEncoding";
    public static final String CONFIG_EMAIL_SMTP_HOST = "host";
    public static final String CONFIG_EMAIL_SMTP_PORT = "port";
    public static final String CONFIG_EMAIL_SMTP_USERNAME = "username";
    public static final String CONFIG_EMAIL_SMTP_PASSWORD = "password";
    public static final String CONFIG_EMAIL_SMTP_TIMEOUT = "timeout";
    public static final String CONFIG_EMAIL_SMTP_AUTH = "auth";
    public static final String CONFIG_EMAIL_SMTP_FROMADDRESS = "fromAddress";
    @Autowired
    private ConfigComponent configComponent;
    private static List<Integer> cachedlist = synchronizedList(new ArrayList<Integer>());
    private static Map<Integer, JavaMailSenderImpl> cachedMap = synchronizedMap(new HashMap<Integer, JavaMailSenderImpl>());

    public JavaMailSender getMailSender(int siteId, Map<String, String> config) {
        JavaMailSenderImpl javaMailSender = cachedMap.get(siteId);
        if (empty(javaMailSender)) {
            javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setDefaultEncoding(config.get(CONFIG_EMAIL_SMTP_DEFAULTENCODING));
            javaMailSender.setHost(config.get(CONFIG_EMAIL_SMTP_HOST));
            javaMailSender.setPort(Integer.parseInt(config.get(CONFIG_EMAIL_SMTP_PORT)));
            javaMailSender.setUsername(config.get(CONFIG_EMAIL_SMTP_USERNAME));
            javaMailSender.setPassword(config.get(CONFIG_EMAIL_SMTP_PASSWORD));
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth", config.get(CONFIG_EMAIL_SMTP_AUTH));
            properties.setProperty("mail.smtp.timeout", config.get(CONFIG_EMAIL_SMTP_TIMEOUT));
            javaMailSender.setJavaMailProperties(properties);
            clearCache(100);
            cachedlist.add(siteId);
            cachedMap.put(siteId, javaMailSender);
        }
        return javaMailSender;
    }

    public String getFromAddress(Map<String, String> config) {
        return config.get(CONFIG_EMAIL_SMTP_FROMADDRESS);
    }

    /**
     * @param toAddress
     * @param title
     * @param content
     * @return
     * @throws MessagingException
     */
    public boolean send(int siteId, String toAddress, String title, String content) throws MessagingException {
        return send(siteId, toAddress, title, content, false);
    }

    /**
     * @param toAddress
     * @param title
     * @param html
     * @return
     * @throws MessagingException
     */
    public boolean sendHtml(int siteId, String toAddress, String title, String html) throws MessagingException {
        return send(siteId, toAddress, title, html, true);
    }

    /**
     * @param toAddress
     * @param fromAddress
     * @param title
     * @param content
     * @param isHtml
     * @return
     * @throws MessagingException
     */
    private boolean send(int siteId, String toAddress, String title, String content, boolean isHtml) throws MessagingException {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE, CONFIG_CODE);
        if (notEmpty(config)) {
            JavaMailSender mailSender = getMailSender(siteId, config);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, DEFAULT_CHARSET);
            messageHelper.setTo(toAddress);
            messageHelper.setFrom(getFromAddress(config));
            messageHelper.setSubject(title);
            messageHelper.setText(content, isHtml);
            SendThread st = new SendThread(mailSender, message);
            st.start();
            return true;
        }
        return false;
    }

    @Override
    public String getCode() {
        return CONFIG_CODE;
    }

    @Override
    public List<ExtendField> getExtendFieldList(String subcode, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<ExtendField>();
        if (CONFIG_SUBCODE.equals(subcode)) {
            extendFieldList.add(new ExtendField(true,
                    getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_SMTP_DEFAULTENCODING), null,
                    CONFIG_EMAIL_SMTP_DEFAULTENCODING, "text", DEFAULT_CHARSET));
            extendFieldList
                    .add(new ExtendField(true, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_SMTP_HOST),
                            null, CONFIG_EMAIL_SMTP_HOST, "text", null));
            extendFieldList
                    .add(new ExtendField(true, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_SMTP_PORT),
                            null, CONFIG_EMAIL_SMTP_PORT, "number", null));
            extendFieldList
                    .add(new ExtendField(true, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_SMTP_USERNAME),
                            null, CONFIG_EMAIL_SMTP_USERNAME, "text", null));
            extendFieldList
                    .add(new ExtendField(true, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_SMTP_PASSWORD),
                            null, CONFIG_EMAIL_SMTP_PASSWORD, "password", null));
            extendFieldList
                    .add(new ExtendField(true, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_SMTP_TIMEOUT),
                            null, CONFIG_EMAIL_SMTP_TIMEOUT, "number", null));
            extendFieldList
                    .add(new ExtendField(true, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_SMTP_AUTH),
                            null, CONFIG_EMAIL_SMTP_AUTH, "boolean", null));
            extendFieldList.add(
                    new ExtendField(true, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_SMTP_FROMADDRESS),
                            null, CONFIG_EMAIL_SMTP_FROMADDRESS, "email", null));
        }
        return extendFieldList;
    }

    @Override
    public List<String> getSubcode(int siteId) {
        List<String> subcodeList = new ArrayList<String>();
        subcodeList.add(CONFIG_SUBCODE);
        return subcodeList;
    }

    @Override
    public String getSubcodeDescription(String subcode, Locale locale) {
        return getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_SUBCODE);
    }

    private void clearCache(int size) {
        if (size < cachedlist.size()) {
            for (int i = 0; i < size / 10; i++) {
                cachedMap.remove(cachedlist.remove(0));
            }
        }
    }

    @Override
    public void clear() {
        cachedlist.clear();
        cachedMap.clear();
    }
}

/**
 * 
 * SendThread 邮件发送线程
 *
 */
class SendThread extends Thread {
    private JavaMailSender mailSender;
    private MimeMessage message;
    private final Log log = getLog(getClass());

    public SendThread(JavaMailSender mailSender, MimeMessage message) {
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
