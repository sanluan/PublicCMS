package com.publiccms.logic.component.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;

/**
 *
 * SiteConfigComponent 站点配置组件
 *
 */
@Component
public class SiteConfigComponent implements Config {
    /**
     * 
     */
    public static final String CONFIG_RETURN_URL = "return_url";
    /**
     * 
     */
    public static final String CONFIG_LOGIN_PATH = "login_path";
    /**
     * 
     */
    public static final String CONFIG_REGISTER_URL = "register_url";
    /**
     * 
     */
    public static final String CONFIG_EXPIRY_MINUTES_WEB = "expiry_minutes.web";
    /**
     * 
     */
    public static final String CONFIG_EXPIRY_MINUTES_MANAGER = "expiry_minutes.manager";
    /**
     * 
     */
    public static final String CONFIG_COMMENT_NEED_CHECK = "comment_need_check";
    /**
     * 
     */
    public static final String CONFIG_STATIC_AFTER_COMMENT = "static_after_comment";
    /**
     * 
     */
    public static final String CONFIG_STATIC_AFTER_SCORE = "static_after_score";
    /**
     * 
     */
    public static final String CONFIG_DEFAULT_CONTENT_STATUS = "default_content_status";
    /**
     * 
     */
    public static final String CONFIG_DEFAULT_CONTENT_USER = "default_content_user";
    /**
     * default expiry minutes
     */
    public static final int DEFAULT_EXPIRY_MINUTES = 30 * 24 * 60;

    @Autowired
    protected ConfigComponent configComponent;

    public String getSafeUrl(String returnUrl, SysSite site, String contextPath) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), CONFIG_CODE_SITE);
        if (isUnSafeUrl(returnUrl, site, config.get(CONFIG_RETURN_URL), contextPath)) {
            return site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        return returnUrl;
    }

    public static boolean isUnSafeUrl(String url, SysSite site, String safeReturnUrl, String contextPath) {
        if (CommonUtils.empty(url)) {
            return true;
        } else if (url.contains("\r") || url.contains("\n")) {
            return true;
        } else if (url.contains("://") || url.startsWith("//")) {
            if (unSafe(url, site, contextPath)) {
                if (CommonUtils.notEmpty(safeReturnUrl)) {
                    for (String safeUrlPrefix : StringUtils.split(safeReturnUrl, CommonConstants.COMMA_DELIMITED)) {
                        if (url.startsWith(safeUrlPrefix)) {
                            return false;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean unSafe(String url, SysSite site, String contextPath) {
        String fixedUrl = url.substring(url.indexOf("://") + 1);
        if (url.startsWith(site.getDynamicPath()) || url.startsWith(site.getSitePath())
                || fixedUrl.startsWith(site.getDynamicPath()) || fixedUrl.startsWith(site.getSitePath())
                || url.startsWith(contextPath + "/")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_RETURN_URL, INPUTTYPE_TEXTAREA, CONFIG_RETURN_URL,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_RETURN_URL)));
        extendFieldList.add(new SysExtendField(CONFIG_REGISTER_URL, INPUTTYPE_TEXT, CONFIG_REGISTER_URL,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_REGISTER_URL)));
        extendFieldList.add(new SysExtendField(CONFIG_LOGIN_PATH, INPUTTYPE_TEXT, CONFIG_LOGIN_PATH,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOGIN_PATH)));
        extendFieldList.add(new SysExtendField(CONFIG_EXPIRY_MINUTES_WEB, INPUTTYPE_NUMBER, false, CONFIG_EXPIRY_MINUTES_WEB,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_EXPIRY_MINUTES_WEB),
                String.valueOf(DEFAULT_EXPIRY_MINUTES)));
        extendFieldList
                .add(new SysExtendField(CONFIG_EXPIRY_MINUTES_MANAGER, INPUTTYPE_NUMBER, false, CONFIG_EXPIRY_MINUTES_MANAGER,
                        getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_EXPIRY_MINUTES_MANAGER),
                        String.valueOf(DEFAULT_EXPIRY_MINUTES)));
        extendFieldList.add(new SysExtendField(CONFIG_COMMENT_NEED_CHECK, INPUTTYPE_BOOLEAN, false, CONFIG_COMMENT_NEED_CHECK,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_COMMENT_NEED_CHECK), "true"));
        extendFieldList
                .add(new SysExtendField(CONFIG_DEFAULT_CONTENT_STATUS, INPUTTYPE_NUMBER, true, CONFIG_DEFAULT_CONTENT_STATUS,
                        getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_DEFAULT_CONTENT_STATUS), "1"));
        extendFieldList.add(new SysExtendField(CONFIG_DEFAULT_CONTENT_USER, INPUTTYPE_USER, CONFIG_DEFAULT_CONTENT_USER,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_DEFAULT_CONTENT_USER)));
        if (site.isUseStatic()) {
            extendFieldList.add(new SysExtendField(CONFIG_STATIC_AFTER_COMMENT, INPUTTYPE_BOOLEAN, false,
                    CONFIG_STATIC_AFTER_COMMENT,
                    getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_STATIC_AFTER_COMMENT), "false"));
            extendFieldList.add(new SysExtendField(CONFIG_STATIC_AFTER_SCORE, INPUTTYPE_BOOLEAN, false, CONFIG_STATIC_AFTER_SCORE,
                    getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_STATIC_AFTER_SCORE), "false"));
        }
        return extendFieldList;
    }
}
