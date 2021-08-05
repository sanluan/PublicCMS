package com.publiccms.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.CorsConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;

/**
 * 
 * CorsContextInterceptor 权限拦截器
 *
 */
@Component
public class CorsInterceptor implements HandlerInterceptor {
    public static final CorsProcessor corsProcessor = new DefaultCorsProcessor();
    @Autowired
    private CorsConfigComponent corsConfigComponent;
    @Autowired
    private SiteComponent siteComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SysSite site = siteComponent.getSite(request.getServerName(),
                UrlPathHelper.defaultInstance.getLookupPathForRequest(request));
        return corsProcessor.processRequest(corsConfigComponent.getConfig(site), request, response);
    }

}
