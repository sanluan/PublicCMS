package com.publiccms.logic.component.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;

/**
 * 
 * SiteAttributeComponent 站点扩展属性组件
 *
 */
@Component
public class SiteAttributeComponent implements Config {
    public static final String CONFIG_LOGO = "logo";
    public static final String CONFIG_SQUARE_LOGO = "square_logo";
    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CommonUtils.joinString(CONFIGPREFIX, CONFIG_CODE_SITEA_TTRIBUTE);

    @Override
    public String getCode(short siteId, boolean showAll) {
        return CONFIG_CODE_SITEA_TTRIBUTE;
    }

    @Override
    public String getCodeDescription(Locale locale) {
        return getMessage(locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_LOGO, INPUTTYPE_IMAGE,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_LOGO)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_LOGO,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_SQUARE_LOGO, INPUTTYPE_IMAGE,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_SQUARE_LOGO)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_SQUARE_LOGO,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        return extendFieldList;
    }

    @Override
    public boolean exportable() {
        return true;
    }
}