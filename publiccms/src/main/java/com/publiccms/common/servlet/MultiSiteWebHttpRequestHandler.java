package com.publiccms.common.servlet;

import static com.publiccms.common.constants.CmsVersion.getVersion;
import static com.publiccms.common.constants.CommonConstants.getDefaultPage;
import static com.publiccms.common.constants.CommonConstants.getXPowered;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.logic.component.site.SiteComponent;
import com.sanluan.common.base.Base;

public class MultiSiteWebHttpRequestHandler extends ResourceHttpRequestHandler {
    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    private SiteComponent siteComponent;

    public MultiSiteWebHttpRequestHandler(SiteComponent siteComponent) {
        this.siteComponent = siteComponent;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader(getXPowered(), getVersion());
        super.handleRequest(request, response);
    }

    @Override
    protected Resource getResource(HttpServletRequest request) throws IOException {
        String path = urlPathHelper.getLookupPathForRequest(request);
        if (path.endsWith(Base.SEPARATOR)) {
            path += getDefaultPage();
        }
        Resource resource = new FileSystemResource(
                siteComponent.getWebFilePath(siteComponent.getSite(request.getServerName()), path));
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            return null;
        }
    }
}
