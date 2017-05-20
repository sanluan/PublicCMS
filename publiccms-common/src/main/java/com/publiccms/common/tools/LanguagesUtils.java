package com.publiccms.common.tools;

import java.util.Locale;

import org.springframework.context.NoSuchMessageException;
import org.springframework.web.context.WebApplicationContext;

import com.publiccms.common.base.Base;

/**
 * 获得国际化信息
 * 
 * LanguagesUtils
 *
 */
public final class LanguagesUtils extends Base {
    public static WebApplicationContext webApplicationContext;

    /**
     * @param locale
     *            语言
     * 
     * @param code
     *            国际化代码
     * @param args
     *            替换参数
     * @return
     */
    public static String getMessage(Locale locale, String code, Object... args) {
        String result;
        try {
            result = webApplicationContext.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            result = code;
        }
        return result;
    }
}
