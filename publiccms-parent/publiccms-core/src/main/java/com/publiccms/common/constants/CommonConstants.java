package com.publiccms.common.constants;

import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;

/**
 *
 * CommonConstants
 * 
 */
public class CommonConstants extends Constants{

    /**
     * CMS文件路径
     */
    public static String CMS_FILEPATH;

    /**
     * 应用上下文
     */
    public static ApplicationContext applicationContext;

    /**
     * 安装锁
     */
    public static final String INSTALL_LOCK_FILENAME = "/install.lock";
    /**
     * 授权文件
     */
    public static final String LICENSE_FILENAME = "/license.dat";
    
    /**
     * 配置文件
     */
    public static final String CMS_CONFIG_FILE = "cms.properties";
    
    /**
     * 加密密钥
     */
    public static final String ENCRYPT_KEY = "publiccms";
    
    /**
     * 公钥
     */
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/ZHiLKHR70/tuoE9TsWhcKuoAgHWIOWrt+kAl1cZotjqsZwR/55PkYkz0SLZMOhXRjY1ii5Y0SDceLG0GsdRQBSvrGb2pPCDPsWM+LuG7BM8Dr8gnHKK1CROVEHJ6wR2RYsD2UIZLRp/HIzTc5B922X9oFibIyZJphFqpZngYtwIDAQAB";
    
    /**
     * 首页模板
     */
    public static final String TEMPLATE_INDEX = "index";
    
    /**
     * 完成模板
     */
    public static final String TEMPLATE_DONE = "common/ajaxDone";
   
    /**
     * 完成并刷新模板
     */
    public static final String TEMPLATE_DONEANDREFRESH = "common/ajaxDoneAndRefresh";
    
    /**
     * 错误模板
     */
    public static final String TEMPLATE_ERROR = "common/ajaxError";
    
    /**
     * 消息
     */
    public static final String MESSAGE = "message";
    
    /**
     * 成功
     */
    public static final String SUCCESS = "success";
    
    /**
     * 错误
     */
    public static final String ERROR = "error";
    
    /**
     * 错误页面
     */
    public static final String ERROR_PAGE = "error.html";
    
    /**
     * json类型
     */
    public static final MediaType jsonMediaType = new MediaType("application", "json", CommonConstants.DEFAULT_CHARSET);
    

    /**
     * @return default page
     */
    public static final String getDefaultPage() {
        return "index.html";
    }
    
    /**
     * @return default subfix
     */
    public static final String getDefaultSubfix() {
        return ".html";
    }

    /**
     * @return session user key
     */
    public static final String getSessionUser() {
        return "user";
    }

    /**
     * @return session user time key
     */
    public static final String getSessionUserTime() {
        return "PUBLICCMS_USER_TIME";
    }

    /**
     * @return session admin key
     */
    public static final String getSessionAdmin() {
        return "admin";
    }

    /**
     * @return cookies user key
     */
    public static final String getCookiesUser() {
        return "PUBLICCMS_USER";
    }
    
    /**
     * @return cookies admin key
     */
    public static final String getCookiesAdmin() {
        return "PUBLICCMS_ADMIN";
    }

    /**
     * @return cookies user key split
     */
    public static final String getCookiesUserSplit() {
        return "_";
    }

    /**
     * @return ueditor page break tag
     */
    public static final String getUeditorPageBreakTag() {
        return "_page_break_tag_";
    }
    
    /**
     * @return ckeditor page break tag
     */
    public static final String getCkeditorPageBreakTag() {
        return "<div style=\"page-break-after:always\"><span style=\"display:none\">&nbsp;</span></div>";
    }
    /**
     * @return kindeditor page break tag
     */
    public static final String getKindEditorPageBreakTag() {
        return "<hr style=\"page-break-after:always;\" class=\"ke-pagebreak\" />";
    }

    /**
     * @return xpowered
     */
    public static final String getXPowered() {
        return "X-Powered-PublicCMS";
    }
}