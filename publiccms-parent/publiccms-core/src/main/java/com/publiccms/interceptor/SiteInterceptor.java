package com.publiccms.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.CorsConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;

/**
 * 
 * SiteInterceptor 权限拦截器
 *
 */
@Component
public class SiteInterceptor implements HandlerInterceptor {
    public static final CorsProcessor corsProcessor = new DefaultCorsProcessor();
    @Resource
    private CorsConfigComponent corsConfigComponent;
    @Resource
    private SiteComponent siteComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SysDomain domain = siteComponent.getDomain(request.getServerName());
        SysSite site = siteComponent.getSite(domain, request.getServerName(),
                UrlPathHelper.defaultInstance.getLookupPathForRequest(request));
        if (site.isMultiple()) {
            String currentSiteId = request.getParameter("currentSiteId");
            if (null != currentSiteId) {
                SysSite newSite = siteComponent.getSiteById(currentSiteId);
                if (null != newSite && null != newSite.getParentId() && newSite.getParentId() == site.getId()) {
                    site = newSite;
                }
            }
        }
        ControllerUtils.setSiteToAttribute(request, site);
        return corsProcessor.processRequest(corsConfigComponent.getConfig(site), request, response);
    }

}
