package com.sanluan.common.tools;

import java.util.Locale;

import org.springframework.context.NoSuchMessageException;

import com.sanluan.common.base.Base;

import config.ApplicationConfig;

/**
 * 
 * LanguagesUtils 获得国际化信息
 *
 */
public final class LanguagesUtils extends Base {
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
            result = ApplicationConfig.webApplicationContext.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            result = code;
        }
        return result;
    }
}
