package com.publiccms.common.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.interceptor.SiteInterceptor;
import com.publiccms.logic.component.config.CorsConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;

/**
 *
 * MultiSiteWebHttpRequestHandler 多站点静态资源处理器
 * 
 */
public class WebFileHttpRequestHandler extends ResourceHttpRequestHandler {
    private SiteComponent siteComponent;
    private CorsConfigComponent corsConfigComponent;
    private static Resource favicon = new ClassPathResource("favicon.ico");

    /**
     * @param siteComponent
     * @param corsConfigComponent
     */
    public WebFileHttpRequestHandler(SiteComponent siteComponent, CorsConfigComponent corsConfigComponent) {
        this.siteComponent = siteComponent;
        this.corsConfigComponent = corsConfigComponent;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader(CommonConstants.getXPowered(), CmsVersion.BASE_VERSION);
        String path = getUrlPathHelper().getLookupPathForRequest(request);
        if (path.endsWith(Constants.SEPARATOR)) {
            path = CommonUtils.joinString(path, CommonConstants.getDefaultPage());
        }
        SysSite site = siteComponent.getSite(request.getServerName(), path);
        ControllerUtils.setSiteToAttribute(request, site);
        SiteInterceptor.corsProcessor.processRequest(corsConfigComponent.getConfig(site), request, response);
        super.handleRequest(request, response);

    }

    @Override
    protected Resource getResource(HttpServletRequest request) throws IOException {
        if (CmsVersion.isInitialized()) {
            SysSite site = ControllerUtils.getSiteFromAttribute(request);
            String path = getUrlPathHelper().getLookupPathForRequest(request);
            if (path.endsWith(Constants.SEPARATOR)) {
                path = CommonUtils.joinString(path, CommonConstants.getDefaultPage());
            }
            path = siteComponent.getPath(site, path);
            Resource resource = new FileSystemResource(siteComponent.getWebFilePath(site.getId(), path));
            if (resource.exists() && (resource.isReadable())) {
                return resource;
            } else if ("/favicon.ico".equals(path) && favicon.exists()) {
                return favicon;
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setUrlPathHelper(UrlPathHelper.defaultInstance);
        if (null == getResourceHttpMessageConverter()) {
            setResourceHttpMessageConverter(new ResourceHttpMessageConverter());
        }
        if (null == getResourceRegionHttpMessageConverter()) {
            setResourceRegionHttpMessageConverter(new ResourceRegionHttpMessageConverter());
        }

    }
}
