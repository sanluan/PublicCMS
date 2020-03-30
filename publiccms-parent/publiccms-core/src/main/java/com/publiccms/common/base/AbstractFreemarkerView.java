package com.publiccms.common.base;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import com.publiccms.common.api.Config;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.view.MultiSiteImportDirective;
import com.publiccms.common.view.MultiSiteIncludeDirective;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.BeanComponent;

/**
 * 
 * AbstractCmsView
 *
 */
public abstract class AbstractFreemarkerView extends FreeMarkerView {
    protected static final String CONTEXT_USER = "user";
    /**
     * Domain Context
     */
    public static final String CONTEXT_DOMAIN = "domain";
    /**
     * Site Context
     */
    public static final String CONTEXT_SITE = "site";
    /**
     * Site Context
     */
    public static final String CONTEXT_SITE_ATTRIBUTE = "siteAttribute";
    /**
     * Base Context
     */
    public static final String CONTEXT_BASE = "base";
    /**
     * Include Context
     */
    public static final String CONTEXT_INCLUDE = "include";
    /**
     * Import Context
     */
    public static final String CONTEXT_IMPORT = "import";

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        exposeAttribute(model, request);
        super.exposeHelpers(model, request);
    }

    /**
     * @param model
     * @param request
     */
    public static void exposeAttribute(Map<String, Object> model, HttpServletRequest request) {
        String serverName = request.getServerName();
        model.put(CONTEXT_BASE, getBasePath(request.getScheme(), request.getServerPort(), serverName, request.getContextPath()));
        model.put(CONTEXT_DOMAIN, BeanComponent.getSiteComponent().getDomain(serverName));
        exposeSite(model, BeanComponent.getSiteComponent().getSite(serverName));
    }

    public static String getBasePath(String scheme, int port, String serverName, String contextPath) {
        String basePath;
        if (80 == port && "http".equals(scheme) || 443 == port && "https".equals(scheme)) {
            basePath = new StringBuilder(scheme).append("://").append(serverName).append(contextPath).toString();
        } else {
            basePath = new StringBuilder(scheme).append("://").append(serverName).append(":").append(port).append(contextPath)
                    .toString();
        }
        return basePath;
    }

    /**
     * @param model
     * @param site
     */
    public static void exposeSite(Map<String, Object> model, SysSite site) {
        model.put(CONTEXT_SITE, site);
        model.put(CONTEXT_SITE_ATTRIBUTE,
                BeanComponent.getConfigComponent().getConfigData(site.getId(), Config.CONFIG_CODE_SITEA_TTRIBUTE));
        model.put(CONTEXT_INCLUDE, new MultiSiteIncludeDirective(site));
        model.put(CONTEXT_IMPORT, new MultiSiteImportDirective(site));
    }

    protected void exposeParameters(Map<String, Object> model, HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String parameterName = parameters.nextElement();
            String[] values = request.getParameterValues(parameterName);
            if (CommonUtils.notEmpty(values)) {
                if (1 < values.length) {
                    RequestUtils.removeCRLF(values);
                    model.put(parameterName, values);
                } else {
                    model.put(parameterName, RequestUtils.removeCRLF(values[0]));
                }
            }
        }
    }
}