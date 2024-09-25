package com.publiccms.logic.component.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;

import jakarta.annotation.Resource;

/**
 *
 * SafeConfigComponent 站点配置组件
 *
 */
@Component
public class SafeConfigComponent implements Config {

    /**
     * config code
     */
    public static final String CONFIG_CODE = "safe";

    /**
     * config description code
     */
    public static final String CONFIG_CODE_DESCRIPTION = CommonUtils.joinString(CONFIGPREFIX, CONFIG_CODE);
    /**
     * web login expiry time
     */
    public static final String CONFIG_EXPIRY_MINUTES_WEB = "expiry_minutes.web";
    /**
     * manager login expiry time
     */
    public static final String CONFIG_EXPIRY_MINUTES_MANAGER = "expiry_minutes.manager";

    /**
     * captcha
     */
    public static final String CONFIG_CAPTCHA = "captcha";
    /**
     * otp login
     */
    public static final String CONFIG_OPT_LOGIN = "enable_otp_login";

    /**
     * allow upload files
     */
    public static final String CONFIG_ALLOW_FILES = "allow_files";

    /**
     * allow access urls
     */
    public static final String CONFIG_ALLOW_URLS = "allow_urls";

    /**
     * allow return urls
     */
    public static final String CONFIG_RETURN_URL = "return_url";

    /**
     * default expiry minutes
     */
    public static final int DEFAULT_EXPIRY_MINUTES = 30 * 24 * 60;
    /**
     * default expiry minutes
     */
    public static final int DEFAULT_EXPIRY_MINUTES_SIGN = 10;
    /**
     * private file key
     */
    public static final String CONFIG_PRIVATEFILE_KEY = "privatefile_key";
    /**
     * private file sign expiry time
     */
    public static final String CONFIG_EXPIRY_MINUTES_SIGN = "expiry_minutes.sign";

    /**
     * captcha config module login
     */
    public static final String CAPTCHA_MODULE_LOGIN = "login";
    /**
     * captcha config module register
     */
    public static final String CAPTCHA_MODULE_REGISTER = "register";
    /**
     * captcha config module management system
     */
    public static final String CAPTCHA_MODULE_MANAGEMENT_SYSTEM = "management_system";
    /**
     * captcha config module comment
     */
    public static final String CAPTCHA_MODULE_COMMENT = "comment";
    /**
     * captcha config module contribute
     */
    public static final String CAPTCHA_MODULE_CONTRIBUTE = "contribute";
    /**
     * captcha config module place contribute
     */
    public static final String CAPTCHA_MODULE_PLACE_CONTRIBUTE = "placeContribute";
    /**
     * captcha config module upload
     */
    public static final String CAPTCHA_MODULE_UPLOAD = "upload";
    /**
     * captcha config module survey
     */
    public static final String CAPTCHA_MODULE_SURVEY = "survey";

    @Resource
    protected ConfigDataComponent configDataComponent;

    /**
     * @param siteId
     * @param showAll
     * @return config code or null
     */
    @Override
    public String getCode(short siteId, boolean showAll) {
        return CONFIG_CODE;
    }

    /**
     * @param locale
     * @return
     */
    @Override
    public String getCodeDescription(Locale locale) {
        return getMessage(locale, CONFIG_CODE_DESCRIPTION);
    }

    public boolean enableCaptcha(short siteId, String module) {
        Map<String, String> config = configDataComponent.getConfigData(siteId, CONFIG_CODE);
        String enableCaptcha = config.get(CONFIG_CAPTCHA);
        return CommonUtils.notEmpty(enableCaptcha)
                && ArrayUtils.contains(StringUtils.split(enableCaptcha, Constants.COMMA), module);
    }

    public boolean enableOtpLogin(short siteId) {
        Map<String, String> config = configDataComponent.getConfigData(siteId, CONFIG_CODE);
        return ConfigDataComponent.getBoolean(config.get(CONFIG_OPT_LOGIN), false);
    }

    public String getSignKey(short siteId) {
        Map<String, String> config = configDataComponent.getConfigData(siteId, CONFIG_CODE);
        String signKey = config.get(CONFIG_PRIVATEFILE_KEY);
        if (CommonUtils.empty(signKey)) {
            signKey = CommonUtils.joinString(siteId, CommonConstants.CMS_FILEPATH.hashCode(), CmsVersion.getClusterId());
        }
        return signKey;
    }

    public String getSafeUrl(String returnUrl, SysSite site, String contextPath) {
        Map<String, String> config = configDataComponent.getConfigData(site.getId(), CONFIG_CODE);
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
        } else if (url.replace("\\", "/").contains("://") || url.replace("\\", "/").startsWith("//")) {
            if (unSafe(url.replace("\\", "/"), site, contextPath)) {
                if (CommonUtils.notEmpty(safeReturnUrl)) {
                    for (String safeUrlPrefix : StringUtils.split(safeReturnUrl, Constants.COMMA)) {
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

    public String[] getSafeSuffix(SysSite site) {
        Map<String, String> config = configDataComponent.getConfigData(site.getId(), CONFIG_CODE);
        String value = config.get(CONFIG_ALLOW_FILES);
        if (CommonUtils.empty(value)) {
            return CmsFileUtils.ALLOW_FILES;
        }
        return StringUtils.split(value, Constants.COMMA);
    }

    public String[] getAllowUrls(SysSite site) {
        Map<String, String> config = configDataComponent.getConfigData(site.getId(), CONFIG_CODE);
        String value = config.get(CONFIG_ALLOW_URLS);
        if (CommonUtils.empty(value)) {
            return new String[] { site.getDynamicPath() };
        }
        return StringUtils.split(value, Constants.COMMA);
    }

    private static boolean unSafe(String url, SysSite site, String contextPath) {
        String fixedUrl = url.substring(url.indexOf("://") + 1);
        return !(url.startsWith(site.getDynamicPath()) || url.startsWith(site.getSitePath())
                || fixedUrl.startsWith(site.getDynamicPath()) || fixedUrl.startsWith(site.getSitePath())
                || url.startsWith(CommonUtils.joinString(contextPath, "/")));
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_EXPIRY_MINUTES_WEB, INPUTTYPE_NUMBER, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_EXPIRY_MINUTES_WEB)),
                null, String.valueOf(DEFAULT_EXPIRY_MINUTES)));
        extendFieldList.add(new SysExtendField(CONFIG_EXPIRY_MINUTES_MANAGER, INPUTTYPE_NUMBER, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_EXPIRY_MINUTES_MANAGER)),
                null, String.valueOf(DEFAULT_EXPIRY_MINUTES)));

        extendFieldList.add(new SysExtendField(CONFIG_PRIVATEFILE_KEY, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PRIVATEFILE_KEY)),
                null));
        extendFieldList.add(new SysExtendField(CONFIG_EXPIRY_MINUTES_SIGN, INPUTTYPE_NUMBER, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_EXPIRY_MINUTES_SIGN)),
                null, String.valueOf(DEFAULT_EXPIRY_MINUTES_SIGN)));

        extendFieldList.add(new SysExtendField(CONFIG_CAPTCHA, INPUTTYPE_CAPTCHA, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_CAPTCHA)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_CAPTCHA,
                        CONFIG_CODE_DESCRIPTION_SUFFIX)),
                null));

        extendFieldList.add(new SysExtendField(CONFIG_OPT_LOGIN, INPUTTYPE_BOOLEAN, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_OPT_LOGIN)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_OPT_LOGIN,
                        CONFIG_CODE_DESCRIPTION_SUFFIX)),
                null));

        extendFieldList.add(new SysExtendField(CONFIG_RETURN_URL, INPUTTYPE_TEXTAREA,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_RETURN_URL)), null));

        extendFieldList.add(new SysExtendField(CONFIG_ALLOW_FILES, INPUTTYPE_TEXTAREA, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_ALLOW_FILES)), null,
                StringUtils.join(CmsFileUtils.ALLOW_FILES, Constants.COMMA)));
        extendFieldList.add(new SysExtendField(CONFIG_ALLOW_URLS, INPUTTYPE_TEXTAREA, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_ALLOW_URLS)), null,
                site.getDynamicPath()));
        return extendFieldList;
    }

    @Override
    public boolean exportable() {
        return true;
    }
}
