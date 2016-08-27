package com.publiccms.logic.component.config;

import static com.sanluan.common.tools.LanguagesUtils.getMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Configable;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.logic.service.sys.SysDomainService;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.base.Base;

@Component
public class LoginConfigComponent extends Base implements Configable {
    public static final String CONFIG_CODE = "login";
    public static final String CONFIG_LOGIN_PATH = "login_path";
    public static final String CONFIG_REGISTER_PATH = "register_path";

    @Autowired
    private SysDomainService domainService;

    @Override
    public String getCode() {
        return CONFIG_CODE;
    }

    @Override
    public List<ExtendField> getExtendFieldList(String subcode, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<ExtendField>();
        extendFieldList.add(new ExtendField(false, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_LOGIN_PATH), null,
                CONFIG_LOGIN_PATH, "template", null));
        extendFieldList.add(new ExtendField(false, getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_REGISTER_PATH),
                null, CONFIG_REGISTER_PATH, "template", null));
        return extendFieldList;
    }

    @Override
    public List<String> getSubcode(int siteId) {
        List<String> subcodeList = new ArrayList<String>();
        @SuppressWarnings("unchecked")
        List<SysDomain> list = (List<SysDomain>) domainService.getPage(siteId, null, null).getList();
        for (SysDomain entity : list) {
            subcodeList.add(entity.getId().toString());
        }
        return subcodeList;
    }

    @Override
    public String getSubcodeDescription(String subcode, Locale locale) {
        try {
            SysDomain entity = domainService.getEntity(Integer.parseInt(subcode));
            if (notEmpty(entity)) {
                return getMessage(locale, CONFIGPREFIX + CONFIG_CODE + "." + "domain", entity.getName());
            } else {
                return BLANK;
            }
        } catch (NumberFormatException e) {
            return BLANK;
        }
    }
}
