package org.publiccms.common.base;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.publiccms.common.view.MultiSiteImportDirective;
import org.publiccms.common.view.MultiSiteIncludeDirective;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.site.SiteComponent;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

/**
 * 
 * AbstractCmsView
 *
 */
public class AbstractFreemarkerView extends FreeMarkerView {
    protected static final String CONTEXT_ADMIN = "admin";
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
    /**
     * Site Component
     */
    public static SiteComponent siteComponent;

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        exposeAttribute(model, request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
        super.exposeHelpers(model, request);
    }

    /**
     * @param model
     * @param scheme
     * @param serverName
     * @param serverPort
     * @param contextPath
     */
    public static void exposeAttribute(Map<String, Object> model, String scheme, String serverName, int serverPort,
            String contextPath) {
        if (80 == serverPort && "http".equals(scheme) || 443 == serverPort && "https".equals(scheme)) {
            model.put(CONTEXT_BASE, new StringBuilder(scheme).append("://").append(serverName).append(contextPath).toString());
        } else {
            model.put(CONTEXT_BASE, new StringBuilder(scheme).append("://").append(serverName).append(":").append(serverPort)
                    .append(contextPath).toString());
        }
        
        model.put(CONTEXT_DOMAIN, siteComponent.getDomain(serverName));
        exposeSite(model, siteComponent.getSite(serverName));
    }

    /**
     * @param model
     * @param site
     */
    public static void exposeSite(Map<String, Object> model, SysSite site) {
        model.put(CONTEXT_SITE, site);
        model.put(CONTEXT_INCLUDE, new MultiSiteIncludeDirective(site));
        model.put(CONTEXT_IMPORT, new MultiSiteImportDirective(site));
    }

    protected void exposeParamters(Map<String, Object> model, HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String paramterName = parameters.nextElement();
            String[] values = request.getParameterValues(paramterName);
            if (notEmpty(values)) {
                if (1 < values.length) {
                    model.put(paramterName, values);
                } else {
                    model.put(paramterName, values[0]);
                }
            }
        }
    }
}