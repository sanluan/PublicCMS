package com.publiccms.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.constants.CommonConstants;
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
        SysSite site = null;
        if (domain.isMultiple()) {
            String currentSiteId = request.getParameter("currentSiteId");
            if (null != currentSiteId) {
                site = siteComponent.getSiteById(currentSiteId);
            }
            if (null == site || (null == site.getParentId() || site.getParentId() != domain.getSiteId())
                    && site.getId() != domain.getSiteId()) {
                site = siteComponent.getSite(domain, request.getServerName(),
                        UrlPathHelper.defaultInstance.getLookupPathForRequest(request));
            }
        } else {
            site = siteComponent.getSite(domain, request.getServerName(), null);
        }
        request.setAttribute(CommonConstants.getAttributeSite(), site);
        return corsProcessor.processRequest(corsConfigComponent.getConfig(site), request, response);
    }

}
