package com.publiccms.common.tools;

import java.util.Locale;

import org.springframework.context.NoSuchMessageException;
import org.springframework.web.context.WebApplicationContext;

/**
 * 获得国际化信息
 * 
 * LanguagesUtils
 *
 */
public final class LanguagesUtils {
    /**
     * @param webApplicationContext 
     * @param locale
     *            语言
     * 
     * @param code
     *            国际化代码
     * @param args
     *            替换参数
     * @return
     */
    public static String getMessage(WebApplicationContext webApplicationContext, Locale locale, String code, Object... args) {
        String result;
        try {
            result = webApplicationContext.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            result = code;
        }
        return result;
    }
}
