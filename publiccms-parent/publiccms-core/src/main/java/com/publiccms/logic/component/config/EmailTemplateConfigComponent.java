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
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.EmailComponent;
import com.publiccms.views.pojo.entities.ExtendField;

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
    public static final String CONFIG_EMAIL_TITLE = "email_title";
    /**
     * 
     */
    public static final String CONFIG_EMAIL_PATH = "email_path";
    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CONFIGPREFIX + EmailComponent.CONFIG_CODE;

    @Autowired
    private ConfigComponent configComponent;

    @Override
    public String getCode(SysSite site) {
        return EmailComponent.CONFIG_CODE;
    }

    @Override
    public String getCodeDescription(SysSite site, Locale locale) {
        return LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<ExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<>();
        Map<String, String> config = configComponent.getConfigData(site.getId(), EmailComponent.CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            extendFieldList.add(new ExtendField(CONFIG_EMAIL_TITLE, INPUTTYPE_TEXT, false,
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                            CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_EMAIL_TITLE),
                    null, null));
            extendFieldList.add(new ExtendField(CONFIG_EMAIL_PATH, INPUTTYPE_TEMPLATE, false,
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                            CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_EMAIL_PATH),
                    null, null));
        }
        return extendFieldList;
    }
}
