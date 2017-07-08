package org.publiccms.common.servlet;

import static org.publiccms.common.constants.CmsVersion.getVersion;
import static org.publiccms.common.constants.CommonConstants.getDefaultPage;
import static org.publiccms.common.constants.CommonConstants.getXPowered;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.publiccms.logic.component.site.SiteComponent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.base.Base;


/**
 *
 * MultiSiteWebHttpRequestHandler
 * 
 */
public class WebFileHttpRequestHandler extends ResourceHttpRequestHandler implements Base {
    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    private SiteComponent siteComponent;

    /**
     * @param siteComponent
     */
    public WebFileHttpRequestHandler(SiteComponent siteComponent) {
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
        if (path.endsWith(SEPARATOR)) {
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
