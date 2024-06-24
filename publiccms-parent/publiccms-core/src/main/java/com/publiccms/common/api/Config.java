package com.publiccms.common.api;

import java.util.List;
import java.util.Locale;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;

/**
 *
 * Config
 * 
 */
public interface Config {
    /**
     * 
     */
    String CONFIGPREFIX = "config.";
    /**
     * 
     */
    String CONFIG_CODE_DESCRIPTION_SUFFIX = CommonUtils.joinString(Constants.DOT, "description");

    /**
     * 
     */
    String INPUTTYPE_TEMPLATE = "template";

    /**
     * 
     */
    String INPUTTYPE_TEXT = "text";

    /**
     * 
     */
    String INPUTTYPE_FILE = "file";
    /**
     * 
     */
    String INPUTTYPE_PRIVATEFILE = "privatefile";

    /**
     * 
     */
    String INPUTTYPE_IMAGE = "image";
    /**
     * 
     */
    String INPUTTYPE_PRIVATEIMAGE = "privateimage";
    /**
     * 
     */
    String INPUTTYPE_VIDEO = "video";
    /**
     * 
     */
    String INPUTTYPE_PRIVATEVIDEO = "privatevideo";

    /**
     * 
     */
    String INPUTTYPE_TEXTAREA = "textarea";

    /**
     * 
     */
    String INPUTTYPE_DATE = "date";

    /**
     * 
     */
    String INPUTTYPE_DATETIME = "datetime";

    /**
     * 
     */
    String INPUTTYPE_TIME = "time";

    /**
     * 
     */
    String INPUTTYPE_USER = "user";

    /**
     * 
     */
    String INPUTTYPE_DEPT = "dept";

    /**
     * 
     */
    String INPUTTYPE_DICTIONARY = "dictionary";

    /**
     * 
     */
    String INPUTTYPE_KEYVALUE = "keyvalue";

    /**
     * 
     */
    String INPUTTYPE_CONTENT = "content";

    /**
     * 
     */
    String INPUTTYPE_CATEGORY = "category";
    /**
     * 
     */
    String INPUTTYPE_VOTE = "vote";
    /**
     * 
     */
    String INPUTTYPE_SURVEY = "survey";
    /**
     * 
     */
    String INPUTTYPE_TAG = "tag";

    /**
     * 
     */
    String INPUTTYPE_CATEGORYTYPE = "categoryType";

    /**
     * 
     */
    String INPUTTYPE_TAGTYPE = "tagType";

    /**
     * 
     */
    String INPUTTYPE_NUMBER = "number";

    /**
     * 
     */
    String INPUTTYPE_PASSWORD = "password";

    /**
     * 
     */
    String INPUTTYPE_BOOLEAN = "boolean";

    /**
     * 
     */
    String INPUTTYPE_EMAIL = "email";
    /**
     * 
     */
    String INPUTTYPE_COLOR = "color";
    /**
     * 
     */
    public static final String INPUTTYPE_MODULE = "module";
    /**
     * 
     */
    public static final String INPUTTYPE_LANG = "lang";
    /**
     * 
     */
    public static final String INPUTTYPE_CATEGORY_PATH = "categoryPath";
    /**
     * 
     */
    public static final String INPUTTYPE_CONTENT_STATUS = "contentStatus";
    /**
     * 
     */
    public static final String INPUTTYPE_KEYWORDS = "keywords";
    /**
     * 
     */
    public static final String INPUTTYPE_CAPTCHA = "captcha";

    public static final String[] INPUT_TYPE_EDITORS = { "ckeditor", "tinymce", "editor" };
    public static final String[] INPUT_TYPE_FILES = { INPUTTYPE_FILE, INPUTTYPE_IMAGE, INPUTTYPE_VIDEO };
    public static final String[] INPUT_TYPE_PRIVATE_FILES = { INPUTTYPE_PRIVATEFILE, INPUTTYPE_PRIVATEIMAGE, INPUTTYPE_PRIVATEVIDEO  };

    /**
     * @param site
     * @param locale
     * @return config extend field list
     */
    List<SysExtendField> getExtendFieldList(SysSite site, Locale locale);

    /**
     * @param siteId
     * @param showAll
     * @return config code or null
     */
    String getCode(short siteId, boolean showAll);

    /**
     * @param locale
     * @param code
     * @param args
     * @return config code or null
     */
    default String getMessage(Locale locale, String code, Object... args) {
        return LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, code, args);
    }

    /**
     * @param siteId
     * @return config code or null
     */
    default String getCode(short siteId) {
        return getCode(siteId, false);
    }

    /**
     * @return exportable
     */
    default boolean exportable() {
        return false;
    }

    /**
     * @param locale
     * @return
     */
    String getCodeDescription(Locale locale);
}