package com.publiccms.logic.component;

import static org.apache.commons.logging.LogFactory.getLog;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.sanluan.common.base.Base;

/**
 * 
 * MailComponent 邮件发送组件
 *
 */
public class MailComponent extends Base {
    private final Log log = getLog(getClass());
    @Autowired
    private JavaMailSender mailSender;
    private String fromAddress;

    /**
     * @param toAddress
     * @param title
     * @param content
     * @return
     */
    public boolean send(String toAddress, String title, String content) {
        return send(toAddress, fromAddress, title, content, false);
    }

    /**
     * @param toAddress
     * @param title
     * @param html
     * @return
     */
    public boolean sendHtml(String toAddress, String title, String html) {
        return send(toAddress, fromAddress, title, html, true);
    }

    /**
     * @param toAddress
     * @param fromAddress
     * @param title
     * @param content
     * @param isHtml
     * @return
     */
    private boolean send(String toAddress, String fromAddress, String title, String content, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, DEFAULT_CHARSET);
            messageHelper.setTo(toAddress);
            messageHelper.setFrom(fromAddress);
            messageHelper.setSubject(title);// 主题
            messageHelper.setText(content, isHtml);// 邮件内容
            SendThread st = new SendThread(mailSender, message);
            st.start();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * @param fromAddress
     *            the fromAddress to set
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
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
