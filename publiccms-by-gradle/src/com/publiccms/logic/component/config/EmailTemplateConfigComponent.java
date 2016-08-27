package com.publiccms.logic.component.config;

import static com.publiccms.logic.component.EmailComponent.CONFIG_CODE;
import static com.sanluan.common.tools.LanguagesUtils.getMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Configable;
import com.publiccms.logic.component.ConfigComponent;
import com.publiccms.logic.component.EmailComponent;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.base.Base;

@Component
public class EmailTemplateConfigComponent extends Base implements Configable {
    public static final String CONFIG_SUBCODE = "template";
    public static final String CONFIG_EMAIL_TITLE = "email_title";
    public static final String CONFIG_EMAIL_PATH = "email_path";

    @Autowired
    private ConfigComponent configComponent;

    @Override
    public String getCode() {
        return CONFIG_CODE;
    }

    @Override
    public List<ExtendField> getExtendFieldList(String subcode, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<ExtendField>();
        if (CONFIG_SUBCODE.equals(subcode)) {
            extendFieldList.add(new ExtendField(false, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_TITLE),
                    null, CONFIG_EMAIL_TITLE, "text", null));
            extendFieldList.add(new ExtendField(false, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_PATH),
                    null, CONFIG_EMAIL_PATH, "template", null));
        }
        return extendFieldList;
    }

    @Override
    public List<String> getSubcode(int siteId) {
        List<String> subcodeList = new ArrayList<String>();
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE, EmailComponent.CONFIG_SUBCODE);
        if (notEmpty(config)) {
            subcodeList.add(CONFIG_SUBCODE);
        }
        return subcodeList;
    }

    @Override
    public String getSubcodeDescription(String subcode, Locale locale) {
        return getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_SUBCODE);
    }
}
