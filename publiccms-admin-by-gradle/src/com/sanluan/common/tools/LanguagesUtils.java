package com.sanluan.common.tools;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

public final class LanguagesUtils {
	/**
	 * 获得国际化信息
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
		WebApplicationContext messageSource = RequestContextUtils.findWebApplicationContext(request);
		if (null == messageSource) {
			throw new IllegalStateException("WebApplicationContext not found!");
		}
		String result = code;
		try {
			result = messageSource.getMessage(code, args, RequestContextUtils.getLocale(request));
		} catch (Exception e) {

		}
		return result;
	}
}
