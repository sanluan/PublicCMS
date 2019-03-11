package com.publiccms.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.publiccms.logic.component.config.CorsConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;

/**
 * 
 * CorsContextInterceptor 权限拦截器
 *
 */
@Component
public class CorsInterceptor extends HandlerInterceptorAdapter {
    public static final CorsProcessor corsProcessor = new DefaultCorsProcessor();
    @Autowired
    private CorsConfigComponent corsConfigComponent;
    @Autowired
    private SiteComponent siteComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return corsProcessor.processRequest(corsConfigComponent.getConfig(siteComponent.getSite(request.getServerName())),
                request, response);
    }
    
}
