package com.publiccms.controller.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateCacheComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;

/**
 * 
 * IndexController 统一分发Controller
 *
 */
@Controller
public class IndexController {
    @Autowired
    private MetadataComponent metadataComponent;
    @Autowired
    private TemplateCacheComponent templateCacheComponent;
    @Autowired
    private ConfigComponent configComponent;
    @Autowired
    private LocaleResolver localeResolver;
    @Autowired
    protected SiteComponent siteComponent;

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    /**
     * REST页面请求统一分发
     * 
     * @param site
     * @param id
     * @param body
     * @param request
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping({ "/**/{id:[0-9]+}" })
    public String rest(@RequestAttribute SysSite site, @PathVariable("id") long id, @RequestBody(required = false) String body,
            HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        return restPage(site, id, null, body, request, response, model);
    }

    /**
     * REST页面请求统一分发
     * 
     * @param site
     * @param id
     * @param pageIndex
     * @param body
     * @param request
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping({ "/**/{id:[0-9]+}_{pageIndex:[0-9]+}" })
    public String restPage(@RequestAttribute SysSite site, @PathVariable("id") long id,
            @PathVariable("pageIndex") Integer pageIndex, @RequestBody(required = false) String body, HttpServletRequest request,
            HttpServletResponse response, ModelMap model) {
        String requestPath = urlPathHelper.getLookupPathForRequest(request);
        if (requestPath.endsWith(CommonConstants.SEPARATOR)) {
            requestPath = requestPath.substring(0, requestPath.lastIndexOf(CommonConstants.SEPARATOR, requestPath.length() - 2))
                    + CommonConstants.getDefaultSubfix();
        } else {
            requestPath = requestPath.substring(0, requestPath.lastIndexOf(CommonConstants.SEPARATOR))
                    + CommonConstants.getDefaultSubfix();
        }
        return getViewName(site, id, pageIndex, requestPath, body, request, response, model);
    }

    /**
     * 页面请求统一分发
     * 
     * @param site
     * @param body
     * @param request
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping({ CommonConstants.SEPARATOR, "/**" })
    public String page(@RequestAttribute SysSite site, @RequestBody(required = false) String body, HttpServletRequest request,
            HttpServletResponse response, ModelMap model) {
        String requestPath = urlPathHelper.getLookupPathForRequest(request);
        if (requestPath.endsWith(CommonConstants.SEPARATOR)) {
            requestPath += CommonConstants.getDefaultPage();
        }
        return getViewName(site, null, null, requestPath, body, request, response, model);
    }

    private String getViewName(SysSite site, Long id, Integer pageIndex, String requestPath, String body,
            HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        SysDomain domain = siteComponent.getDomain(request.getServerName());
        String fullRequestPath = siteComponent.getViewName(site, domain, requestPath);
        String templatePath = siteComponent.getWebTemplateFilePath() + fullRequestPath;
        CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(templatePath);
        if (metadata.isUseDynamic()) {
            if (metadata.isNeedLogin() && null == ControllerUtils.getUserFromSession(request.getSession())) {
                Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
                String loginPath = config.get(LoginConfigComponent.CONFIG_LOGIN_PATH);
                StringBuilder sb = new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX);
                if (CommonUtils.notEmpty(loginPath)) {
                    return sb.append(loginPath).append("?returnUrl=")
                            .append(RequestUtils.getEncodePath(requestPath, request.getQueryString())).toString();
                } else {
                    return sb.append(site.getDynamicPath()).toString();
                }
            }
            String[] acceptParameters = StringUtils.split(metadata.getAcceptParameters(), CommonConstants.COMMA_DELIMITED);
            if (CommonUtils.notEmpty(acceptParameters)) {
                billingRequestParametersToModel(request, acceptParameters, model);
                if (null != id && ArrayUtils.contains(acceptParameters, "id")) {
                    model.addAttribute("id", id.toString());
                    if (null != pageIndex && ArrayUtils.contains(acceptParameters, "pageIndex")) {
                        model.addAttribute("pageIndex", pageIndex.toString());
                    }
                }
            }
            CmsPageData data = metadataComponent.getTemplateData(
                    siteComponent.getWebTemplateFilePath() + siteComponent.getCurrentViewNamePrefix(site, domain) + requestPath);
            model.addAttribute("metadata", metadata.getAsMap(data));
            if (metadata.isNeedBody()) {
                model.addAttribute("body", body);
            }
            if (CommonUtils.notEmpty(metadata.getContentType())) {
                response.setContentType(metadata.getContentType());
            }
            if (CommonUtils.notEmpty(metadata.getCacheTime()) && 0 < metadata.getCacheTime()) {
                int cacheMillisTime = metadata.getCacheTime() * 1000;
                String cacheControl = request.getHeader("Cache-Control");
                String pragma = request.getHeader("Pragma");
                if (CommonUtils.notEmpty(cacheControl) && "no-cache".equalsIgnoreCase(cacheControl)
                        || CommonUtils.notEmpty(pragma) && "no-cache".equalsIgnoreCase(pragma)) {
                    cacheMillisTime = 0;
                }
                return templateCacheComponent.getCachedPath(requestPath, fullRequestPath, localeResolver.resolveLocale(request),
                        cacheMillisTime, acceptParameters, request, model);
            }
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
            }
        }
        return requestPath;
    }

    private static void billingRequestParametersToModel(HttpServletRequest request, String[] acceptParameters, ModelMap model) {
        for (String parameterName : acceptParameters) {
            String[] values = request.getParameterValues(parameterName);
            if (CommonUtils.notEmpty(values)) {
                if (1 < values.length) {
                    model.addAttribute(parameterName, values);
                } else {
                    model.addAttribute(parameterName, values[0]);
                }
            }
        }
    }
}
