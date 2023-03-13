package com.publiccms.logic.component.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.BeanComponent;
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
    public static final String CONFIG_CODE_DESCRIPTION = CommonUtils.joinString(CONFIGPREFIX, CONFIG_CODE);
    /**
     * default expiry minutes
     */
    public static final int DEFAULT_EXPIRY_MINUTES = 30;

    @Override
    public String getCode(short siteId, boolean showAll) {
        Map<String, String> config = BeanComponent.getConfigComponent().getConfigData(siteId, EmailComponent.CONFIG_CODE);
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
        Map<String, String> config = BeanComponent.getConfigComponent().getConfigData(site.getId(), EmailComponent.CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            List<SysExtendField> extendFieldList = new ArrayList<>();
            extendFieldList.add(new SysExtendField(CONFIG_EMAIL_TITLE, INPUTTYPE_TEXT,
                    getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_EMAIL_TITLE)),
                    null));
            extendFieldList.add(new SysExtendField(CONFIG_EMAIL_PATH, INPUTTYPE_TEMPLATE,
                    getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_EMAIL_PATH)), null));
            extendFieldList.add(new SysExtendField(CONFIG_EXPIRY_MINUTES, INPUTTYPE_NUMBER, false,
                    getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_EXPIRY_MINUTES)),
                    null, "30"));
            return extendFieldList;
        } else {
            return null;
        }
    }

    @Override
    public boolean exportable() {
        return true;
    }
}
