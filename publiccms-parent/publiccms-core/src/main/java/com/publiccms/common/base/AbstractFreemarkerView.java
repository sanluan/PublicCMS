package com.publiccms.common.base;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.api.Config;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.view.MultiSiteImportDirective;
import com.publiccms.common.view.MultiSiteIncludeDirective;
import com.publiccms.common.view.SafeRequestContext;
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
    /**
     * Domain Context
     */
    public static final String CONTEXT_ADMIN_CONTEXT_PATH = "adminContextPath";

    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        model.put(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, new SafeRequestContext(request, response, getServletContext(), model));
        super.doRender(model, request, response);
    }

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        exposeAttribute(model, request);
        super.exposeHelpers(model, request);
    }

    /**
     * @param model
     * @param admin
     * @param request
     */
    public static void exposeAttribute(Map<String, Object> model, HttpServletRequest request) {
        String serverName = request.getServerName();
        model.put(CONTEXT_BASE, request.getContextPath());
        model.put(CONTEXT_DOMAIN, BeanComponent.getSiteComponent().getDomain(serverName));
        SysSite site = ControllerUtils.getSiteFromAttribute(request);
        if (null == site) {
            site = BeanComponent.getSiteComponent().getSite(serverName,
                    UrlPathHelper.defaultInstance.getLookupPathForRequest(request));
        }
        exposeSite(model, site);
    }

    /**
     * @param model
     * @param admin
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