package com.sanluan.common.tools;

import static org.springframework.web.servlet.support.RequestContextUtils.findWebApplicationContext;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.NoSuchMessageException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.sanluan.common.base.Base;

/**
 * 
 * LanguagesUtils 获得国际化信息
 *
 */
public final class LanguagesUtils extends Base {
    /**
     * 
     * @param request
     *            HttpServletRequest
     * @param code
     *            国际化代码
     * @param args
     *            替换参数
     * @return
     * @see org.springframework.context.MessageSource#getMessage(String,
     *      Object[], Locale)
     */
    public static String getMessage(HttpServletRequest request, String code, Object... args) {
        WebApplicationContext messageSource = findWebApplicationContext(request);
        String result;
        if (notEmpty(messageSource)) {
            try {
                result = messageSource.getMessage(code, args, RequestContextUtils.getLocale(request));
            } catch (NoSuchMessageException e) {
                result = code;
            }
        } else {
            result = code;
        }
        return result;
    }
}
