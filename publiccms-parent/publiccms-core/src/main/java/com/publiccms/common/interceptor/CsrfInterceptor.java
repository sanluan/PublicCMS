package com.publiccms.common.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.LanguagesUtils;

public class CsrfInterceptor extends HandlerInterceptorAdapter {
    protected Map<HandlerMethod, CsrfCache> methodCache = new HashMap<>();
    private CsrfCache DEFAULT_CACHE = new CsrfCache(false, null);
    private boolean admin = false;

    /**
     * @param admin
     */
    public CsrfInterceptor(boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        if (handler instanceof HandlerMethod) {
            CsrfCache cache = methodCache.computeIfAbsent((HandlerMethod) handler, k -> {
                Csrf csrf = k.getMethodAnnotation(Csrf.class);
                if (null == csrf) {
                    return DEFAULT_CACHE;
                } else {
                    return new CsrfCache(true, csrf.field());
                }
            });
            if (cache.isEnable()) {
                String value = request.getParameter(cache.getParameterName());
                String token = admin ? ControllerUtils.getAdminToken(request) : ControllerUtils.getWebToken(request);
                if (null == value || !value.equals(token)) {
                    try {
                        String message = LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                                "verify.notEquals._csrf");
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
                    } catch (IOException e) {
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
