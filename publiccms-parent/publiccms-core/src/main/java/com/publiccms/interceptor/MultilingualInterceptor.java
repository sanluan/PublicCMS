package com.publiccms.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.RequestUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * MultilingualInterceptor 多语言切换拦截器
 * 
 */
@Component
public class MultilingualInterceptor implements HandlerInterceptor {
    public static final String DEFAULT_PARAM_NAME = "lang";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        String newLanguage = request.getParameter(DEFAULT_PARAM_NAME);
        if (newLanguage != null) {
            request.setAttribute(CommonConstants.getCookiesLanguage(), newLanguage);
            RequestUtils.addCookie(request.getContextPath(), request.getScheme(), response, CommonConstants.getCookiesLanguage(),
                    newLanguage, Integer.MAX_VALUE, null);
        }
        return true;
    }
}
