package com.publiccms.common.api;

import java.util.List;
import java.util.Locale;

import com.publiccms.entities.sys.SysSite;
import com.publiccms.views.pojo.entities.ExtendField;

/**
 *
 * Config
 * 
 */
public interface Config {
    /**
     * 
     */
    public static final String CONFIGPREFIX = "config.";

    /**
     * 
     */
    public static final String CONFIG_CODE_SITE = "site";
    /**
     * 
     */
    public static final String CONFIG_CODE_SITEA_TTRIBUTE = "siteAttribute";

    /**
     * 
     */
    public static final String INPUTTYPE_TEMPLATE = "template";

    /**
     * 
     */
    public static final String INPUTTYPE_TEXT = "text";

    /**
     * 
     */
    public static final String INPUTTYPE_FILE = "file";

    /**
     * 
     */
    public static final String INPUTTYPE_IMAGE = "image";

    /**
     * 
     */
    public static final String INPUTTYPE_EDITOR = "editor";

    /**
     * 
     */
    public static final String INPUTTYPE_TEXTAREA = "textarea";

    /**
     * 
     */
    public static final String INPUTTYPE_DATE = "date";

    /**
     * 
     */
    public static final String INPUTTYPE_DATETIME = "datetime";

    /**
     * 
     */
    public static final String INPUTTYPE_USER = "user";

    /**
     * 
     */
    public static final String INPUTTYPE_CONTENT = "content";
    /**
     * 
     */
    public static final String INPUTTYPE_CATEGORY = "category";
    /**
     * 
     */
    public static final String INPUTTYPE_DICTIONARY = "dictionary";

    /**
     * 
     */
    public static final String INPUTTYPE_NUMBER = "number";

    /**
     * 
     */
    public static final String INPUTTYPE_PASSWORD = "password";

    /**
     * 
     */
    public static final String INPUTTYPE_BOOLEAN = "boolean";

    /**
     * 
     */
    public static final String INPUTTYPE_EMAIL = "email";

    /**
     * @param site
     * @return config code
     */
    public String getCode(SysSite site);

    /**
     * @param site
     * @param locale
     * @return config code description
     */
    public String getCodeDescription(SysSite site, Locale locale);

    /**
     * @param site
     * @param locale
     * @return config extend field list
     */
    public List<ExtendField> getExtendFieldList(SysSite site, Locale locale);
}