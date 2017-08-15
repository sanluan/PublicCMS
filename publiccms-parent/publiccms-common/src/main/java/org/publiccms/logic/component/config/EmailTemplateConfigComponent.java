package org.publiccms.logic.component.config;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.LanguagesUtils.getMessage;
import static org.publiccms.common.constants.CommonConstants.webApplicationContext;
import static org.publiccms.logic.component.site.EmailComponent.CONFIG_CODE;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.publiccms.common.api.Config;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.views.pojo.ExtendField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.Base;

/**
 *
 * EmailTemplateConfigComponent 邮件模板配置组件
 *
 */
@Component
public class EmailTemplateConfigComponent implements Config, Base {
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
    public static final String CONFIG_CODE_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE;

    @Autowired
    private ConfigComponent configComponent;

    @Override
    public String getCode(SysSite site) {
        return CONFIG_CODE;
    }

    @Override
    public String getCodeDescription(SysSite site, Locale locale) {
        return getMessage(webApplicationContext, locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<ExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<>();
        Map<String, String> config = configComponent.getConfigData(site.getId(), CONFIG_CODE);
        if (notEmpty(config)) {
            extendFieldList.add(new ExtendField(CONFIG_EMAIL_TITLE, INPUTTYPE_TEXT, false,
                    getMessage(webApplicationContext, locale, CONFIG_CODE_DESCRIPTION + DOT + CONFIG_EMAIL_TITLE), null, null));
            extendFieldList.add(new ExtendField(CONFIG_EMAIL_PATH, INPUTTYPE_TEMPLATE, false,
                    getMessage(webApplicationContext, locale, CONFIG_CODE_DESCRIPTION + DOT + CONFIG_EMAIL_PATH), null, null));
        }
        return extendFieldList;
    }
}
