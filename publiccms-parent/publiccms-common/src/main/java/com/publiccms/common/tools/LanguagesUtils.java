package com.publiccms.common.tools;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;

/**
 * 获得国际化信息
 * 
 * LanguagesUtils
 *
 */
public final class LanguagesUtils {
    /**
     * @param applicationContext 
     * @param locale
     *            语言
     * 
     * @param code
     *            国际化代码
     * @param args
     *            替换参数
     * @return international message
     */
    public static String getMessage(ApplicationContext applicationContext, Locale locale, String code, Object... args) {
        String result;
        try {
            result = applicationContext.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            result = code;
        }
        return result;
    }
}
