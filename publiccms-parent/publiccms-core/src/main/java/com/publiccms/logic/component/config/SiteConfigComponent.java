package com.publiccms.logic.component.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsContentService;

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
    public static final String CONFIG_CODE = "site";

    /**
     *
     */
    public static final String CONFIG_CODE_DESCRIPTION = CommonUtils.joinString(CONFIGPREFIX, CONFIG_CODE);

    /**
     * login url
     */
    public static final String CONFIG_LOGIN_PATH = "login_path";
    /**
     * register url
     */
    public static final String CONFIG_REGISTER_URL = "register_url";
    /**
     * pdfviewer url
     */
    public static final String CONFIG_PDFVIEWER_URL = "pdfviewer_url";
    /**
     * site exclude module
     */
    public static final String CONFIG_SITE_EXCLUDE_MODULE = "site_exclude_module";
    /**
     * site exclude lang
     */
    public static final String CONFIG_SITE_EXCLUDE_LANG = "site_exclude_lang";
    /**
     * category template path
     */
    public static final String CONFIG_CATEGORY_TEMPLATE_PATH = "category_template_path";
    /**
     * category path
     */
    public static final String CONFIG_CATEGORY_PATH = "category_path";
    /**
     * max image width
     */
    public static final String CONFIG_MAX_IMAGE_WIDTH = "max_image_width";
    /**
     * comment need check
     */
    public static final String CONFIG_COMMENT_NEED_CHECK = "comment_need_check";
    /**
     * static after comment
     */
    public static final String CONFIG_STATIC_AFTER_COMMENT = "static_after_comment";
    /**
     * static after score
     */
    public static final String CONFIG_STATIC_AFTER_SCORE = "static_after_score";
    /**
     * max scores
     */
    public static final String CONFIG_MAX_SCORES = "max_scores";
    /**
     * default content statuc
     */
    public static final String CONFIG_DEFAULT_CONTENT_STATUS = "default_content_status";
    /**
     * default content user
     */
    public static final String CONFIG_DEFAULT_CONTENT_USER = "default_content_user";

    /**
     * default max scores
     */
    public static final int DEFAULT_MAX_SCORES = 5;

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
        return LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_SITE_EXCLUDE_MODULE, INPUTTYPE_MODULE,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_SITE_EXCLUDE_MODULE)),
                null));
        extendFieldList.add(new SysExtendField(CONFIG_SITE_EXCLUDE_LANG, INPUTTYPE_LANG,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_SITE_EXCLUDE_LANG)),
                null));

        extendFieldList.add(new SysExtendField(CONFIG_REGISTER_URL, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_REGISTER_URL)), null));
        extendFieldList.add(new SysExtendField(CONFIG_LOGIN_PATH, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_LOGIN_PATH)), null));
        extendFieldList.add(new SysExtendField(CONFIG_PDFVIEWER_URL, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PDFVIEWER_URL)), null));

        if (site.isUseStatic()) {
            extendFieldList.add(new SysExtendField(CONFIG_CATEGORY_TEMPLATE_PATH, INPUTTYPE_TEMPLATE, false,
                    getMessage(locale,
                            CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_CATEGORY_TEMPLATE_PATH)),
                    null, "category/staticCategory.html"));
        }
        extendFieldList.add(new SysExtendField(CONFIG_CATEGORY_PATH, INPUTTYPE_CATEGORY_PATH, true,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_CATEGORY_PATH)), null,
                site.isUseStatic() ? "category/${category.code}.html" : "content/category.html?id=${category.id}"));

        extendFieldList.add(new SysExtendField(CONFIG_MAX_IMAGE_WIDTH, INPUTTYPE_NUMBER,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_MAX_IMAGE_WIDTH)),
                null));

        extendFieldList.add(new SysExtendField(CONFIG_DEFAULT_CONTENT_STATUS, INPUTTYPE_CONTENT_STATUS, true,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_DEFAULT_CONTENT_STATUS)),
                null, String.valueOf(CmsContentService.STATUS_PEND)));
        extendFieldList.add(new SysExtendField(CONFIG_DEFAULT_CONTENT_USER, INPUTTYPE_USER,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_DEFAULT_CONTENT_USER)),
                null));

        extendFieldList.add(new SysExtendField(CONFIG_COMMENT_NEED_CHECK, INPUTTYPE_BOOLEAN, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_COMMENT_NEED_CHECK)),
                null, "true"));
        extendFieldList.add(new SysExtendField(CONFIG_MAX_SCORES, INPUTTYPE_NUMBER, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_MAX_SCORES)), null,
                String.valueOf(DEFAULT_MAX_SCORES)));
        if (site.isUseStatic()) {
            extendFieldList.add(new SysExtendField(CONFIG_STATIC_AFTER_COMMENT, INPUTTYPE_BOOLEAN, false,
                    getMessage(locale,
                            CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_STATIC_AFTER_COMMENT)),
                    null, "false"));
            extendFieldList.add(new SysExtendField(CONFIG_STATIC_AFTER_SCORE, INPUTTYPE_BOOLEAN, false,
                    getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_STATIC_AFTER_SCORE)),
                    null, "false"));
        }
        return extendFieldList;
    }

    @Override
    public boolean exportable() {
        return true;
    }
}
