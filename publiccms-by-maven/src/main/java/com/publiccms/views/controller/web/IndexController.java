package com.publiccms.views.controller.web;

import static com.publiccms.logic.component.config.LoginConfigComponent.CONFIG_CODE;
import static com.publiccms.logic.component.config.LoginConfigComponent.CONFIG_LOGIN_PATH;
import static com.sanluan.common.tools.RequestUtils.getEncodePath;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.ConfigComponent;
import com.publiccms.logic.component.MetadataComponent;
import com.publiccms.logic.component.TemplateCacheComponent;
import com.publiccms.views.pojo.CmsPageMetadata;
import com.sanluan.common.servlet.PageNotFoundException;

/**
 * 
 * IndexController 统一分发Controller
 *
 */
@Controller
public class IndexController extends AbstractController {
    @Autowired
    private MetadataComponent metadataComponent;
    @Autowired
    private TemplateCacheComponent templateCacheComponent;
    @Autowired
    private ConfigComponent configComponent;
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    /**
     * 页面请求统一分发
     * 
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping({ SEPARATOR, "/**" })
    public String page(HttpServletRequest request, HttpSession session, ModelMap model) throws PageNotFoundException {
        String requestPath = urlPathHelper.getLookupPathForRequest(request);
        if (SEPARATOR.equals(requestPath) || requestPath.endsWith(SEPARATOR)) {
            requestPath += "index.html";
        }
        SysDomain domain = getDomain(request);
        SysSite site = getSite(request);
        String realRequestPath = siteComponent.getViewNamePreffix(site, domain) + requestPath;
        String templatePath = siteComponent.getWebTemplateFilePath()+ realRequestPath;
        CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(templatePath);
        if (metadata.isUseDynamic()) {
            if (metadata.isNeedLogin() && notEmpty(domain.getId()) && empty(getUserFromSession(session))) {
                Map<String, String> config = configComponent.getConfigData(site.getId(), CONFIG_CODE, domain.getId().toString());
                String loginPath = config.get(CONFIG_LOGIN_PATH);
                if (notEmpty(loginPath)) {
                    return REDIRECT + loginPath + "?returnUrl=" + getEncodePath(requestPath, request.getQueryString());
                } else {
                    return REDIRECT + site.getSitePath();
                }
            }
            model.put("metadata", metadata);
            if (notEmpty(metadata.getAcceptParamters())) {
                billingRequestParamtersToModel(request, metadata.getAcceptParamters(), model);
            }
            if (notEmpty(metadata.getCacheTime()) && 10 <= metadata.getCacheTime()) {
                return templateCacheComponent.getCachedPath(requestPath, realRequestPath, metadata.getCacheTime() * 1000,
                        metadata.getAcceptParamters(), site, request, model);
            }
        } else {
            throw new PageNotFoundException(requestPath);
        }
        return requestPath;
    }

    private void billingRequestParamtersToModel(HttpServletRequest request, String acceptParamters, ModelMap model) {
        for (String paramterName : split(acceptParamters, COMMA_DELIMITED)) {
            String[] values = request.getParameterValues(paramterName);
            if (isNotEmpty(values)) {
                if (1 < values.length) {
                    model.put(paramterName, values);
                } else {
                    model.put(paramterName, values[0]);
                }
            }
        }
    }
}
