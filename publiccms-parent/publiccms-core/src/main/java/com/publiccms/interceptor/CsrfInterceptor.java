package com.publiccms.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.LanguagesUtils;

public class CsrfInterceptor implements HandlerInterceptor {
    protected Map<HandlerMethod, CsrfCache> methodCache = new HashMap<>();
    private CsrfCache DEFAULT_CACHE = new CsrfCache(false, null);
    private boolean admin = false;
    @Resource
    private LocaleResolver localeResolver;

    /**
     * @param admin
     */
    public CsrfInterceptor(boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        if (handler instanceof HandlerMethod) {
            CsrfCache cache = methodCache.get((HandlerMethod) handler);
            if (null == cache) {
                Csrf csrf = ((HandlerMethod) handler).getMethodAnnotation(Csrf.class);
                if (null == csrf) {
                    cache = DEFAULT_CACHE;
                } else {
                    cache = new CsrfCache(true, csrf.field());
                }
            }
            methodCache.put((HandlerMethod) handler, cache);
            if (cache.isEnable()) {
                String value = request.getParameter(cache.getParameterName());
                String token = admin ? ControllerUtils.getAdminToken(request) : ControllerUtils.getWebToken(request);
                if (null == value || !value.equals(token)) {
                    try {
                        String message = LanguagesUtils.getMessage(CommonConstants.applicationContext, localeResolver.resolveLocale(request),
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
