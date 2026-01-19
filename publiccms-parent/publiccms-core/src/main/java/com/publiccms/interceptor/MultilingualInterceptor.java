package com.publiccms.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.RequestUtils;

public class MultilingualInterceptor implements HandlerInterceptor {
    public static final String DEFAULT_PARAM_NAME = "language";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        String newLanguage = request.getParameter(DEFAULT_PARAM_NAME);
        if (newLanguage != null) {
            RequestUtils.addCookie(request.getContextPath(), request.getScheme(), response, CommonConstants.getCookiesLanguage(),
                    newLanguage, Integer.MAX_VALUE, null);
        }
        return true;
    }
}
