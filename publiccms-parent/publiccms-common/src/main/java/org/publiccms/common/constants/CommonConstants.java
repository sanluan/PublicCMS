package org.publiccms.common.constants;

import org.springframework.web.context.WebApplicationContext;

/**
 *
 * CommonConstants
 * 
 */
public class CommonConstants {

    /**
     * CMS文件路径
     */
    public static String CMS_FILEPATH;

    /**
     * 安装锁
     */
    public static final String INSTALL_LOCK_FILENAME = "/install.lock";

    /**
     * 管理后台路径 Management Path
     */
    public static final String ADMIN_BASE_PATH = "/admin";
    
    /**
     * 应用上下文
     */
    public static WebApplicationContext webApplicationContext;

    /**
     * @return
     */
    public static final String getDefaultPage() {
        return "index.html";
    }

    /**
     * @return
     */
    public static final String getSessionUser() {
        return "PUBLICCMS_USER";
    }

    /**
     * @return
     */
    public static final String getSessionUserTime() {
        return "PUBLICCMS_USER_TIME";
    }

    /**
     * @return
     */
    public static final String getSessionAdmin() {
        return "PUBLICCMS_ADMIN";
    }

    /**
     * @return
     */
    public static final String getCookiesUser() {
        return "PUBLICCMS_USER";
    }

    /**
     * @return
     */
    public static final String getCookiesUserSplit() {
        return "##";
    }

    /**
     * @return
     */
    public static final String getDefaultPageBreakTag() {
        return "_page_break_tag_";
    }

    /**
     * @return
     */
    public static final String getXPowered() {
        return "X-Powered-PublicCMS";
    }
}