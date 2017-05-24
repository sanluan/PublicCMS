package org.publiccms.logic.component.config;

import static com.publiccms.common.tools.LanguagesUtils.getMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.publiccms.common.api.Config;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.views.pojo.ExtendField;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.Base;

/**
 *
 * LoginConfigComponent 登陆配置组件
 *
 */
@Component
public class LoginConfigComponent extends Base implements Config {
    /**
     * 
     */
    public static final String CONFIG_LOGIN_PATH = "login_path";
    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE_SITE;

    @Override
    public String getCode(SysSite site) {
        return CONFIG_CODE_SITE;
    }

    @Override
    public String getCodeDescription(SysSite site, Locale locale) {
        return getMessage(locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<ExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<ExtendField>();
        extendFieldList.add(new ExtendField(CONFIG_LOGIN_PATH, INPUTTYPE_TEXT, false,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + DOT + CONFIG_LOGIN_PATH), null, null));
        return extendFieldList;
    }
}
