package com.publiccms.logic.component.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.EmailComponent;

/**
 *
 * EmailTemplateConfigComponent 邮件模板配置组件
 *
 */
@Component
public class EmailTemplateConfigComponent implements Config {
    /**
     * 
     */
    public static final String CONFIG_CODE = "email_verification";
    /**
     * 
     */
    public static final String CONFIG_EMAIL_TITLE = "email_title";
    /**
     * 
     */
    public static final String CONFIG_EMAIL_PATH = "email_path";
    /**
     * 
     */
    public static final String CONFIG_EXPIRY_MINUTES = "expiry_minutes";
    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE;
    /**
     * default expiry minutes
     */
    public static final int DEFAULT_EXPIRY_MINUTES = 30;

    @Autowired
    private ConfigComponent configComponent;

    @Override
    public String getCode(SysSite site, boolean showAll) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), EmailComponent.CONFIG_CODE);
        if (CommonUtils.notEmpty(config) || showAll) {
            return CONFIG_CODE;
        } else {
            return null;
        }
    }

    @Override
    public String getCodeDescription(Locale locale) {
        return getMessage(locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), EmailComponent.CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            List<SysExtendField> extendFieldList = new ArrayList<>();
            extendFieldList.add(new SysExtendField(CONFIG_EMAIL_TITLE, INPUTTYPE_TEXT, CONFIG_EMAIL_TITLE,
                    getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_EMAIL_TITLE)));
            extendFieldList.add(new SysExtendField(CONFIG_EMAIL_PATH, INPUTTYPE_TEMPLATE, CONFIG_EMAIL_PATH,
                    getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_EMAIL_PATH)));
            extendFieldList.add(new SysExtendField(CONFIG_EXPIRY_MINUTES, INPUTTYPE_NUMBER, false, CONFIG_EXPIRY_MINUTES,
                    getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_EXPIRY_MINUTES), "30"));
            return extendFieldList;
        } else {
            return null;
        }
    }
}
