package com.publiccms.common.view;

import static com.publiccms.logic.component.SiteComponent.expose;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import com.publiccms.logic.component.SiteComponent;

/**
 * 
 * InitializeFreeMarkerView
 *
 */
public class InitializeFreeMarkerView extends FreeMarkerView {
    protected static final String CONTEXT_ADMIN = "admin";
    protected static final String CONTEXT_USER = "user";
    public static SiteComponent siteComponent;

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        expose(model, siteComponent.getSite(request.getServerName(), request.getServerPort()), request.getScheme(),
                request.getServerName(), request.getServerPort(), request.getContextPath());
        super.exposeHelpers(model, request);
    }
}