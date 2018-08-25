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
     * Site Context
     */
    public static final String CONTEXT_SITE_ATTRIBUTE = "siteAttribute";
    /**
     * Base Context
     */
    public static final String CONTEXT_BASE = "base";
    /**
     * Base Context
     */
    public static final String CONTEXT_ADMNIN_BASE = "adminBase";
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
     * @param scheme
     * @param serverName
     * @param serverPort
     * @param contextPath
     */
    public static void exposeAttribute(Map<String, Object> model, HttpServletRequest request) {
        String schema = RequestUtils.getSchema(request);
        if (80 == request.getServerPort() && "http".equals(schema) || 443 == request.getServerPort() && "https".equals(schema)) {
            model.put(CONTEXT_BASE, new StringBuilder(schema).append("://").append(request.getServerName())
                    .append(request.getContextPath()).toString());
        } else {
            model.put(CONTEXT_BASE, new StringBuilder(schema).append("://").append(request.getServerName()).append(":")
                    .append(request.getServerPort()).append(request.getContextPath()).toString());
        }

        model.put(CONTEXT_DOMAIN, BeanComponent.getSiteComponent().getDomain(request.getServerName()));
        exposeSite(model, BeanComponent.getSiteComponent().getSite(request.getServerName()));
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
                    model.put(parameterName, values);
                } else {
                    model.put(parameterName, values[0]);
                }
            }
        }
    }
}