package com.publiccms.views.controller.web;

import static com.sanluan.common.tools.RequestUtils.getEncodePath;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
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
    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    public final static String INTERFACE_NOT_FOUND = "interface_not_found";
    public static final Map<String, String> NOT_FOUND_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("error", INTERFACE_NOT_FOUND);
        }
    };

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
        SysSite site = getSite(request);
        String templatePath = siteComponent.getWebTemplateFilePath(site, requestPath);
        CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(templatePath);
        if (notEmpty(metadata)) {
            if (metadata.isUseDynamic()) {
                SysDomain domain = getDomain(request);
                if (metadata.isNeedLogin() && empty(getUserFromSession(session))) {
                    return REDIRECT + domain.getLoginPath() + "?returnUrl="
                            + getEncodePath(requestPath, request.getQueryString());
                }
                model.put("metadata", metadata);
                if (notEmpty(metadata.getAcceptParamters())) {
                    billingRequestParamtersToModel(request, metadata.getAcceptParamters(), model);
                }
                if (notEmpty(metadata.getCacheTime()) && 10 <= metadata.getCacheTime()) {
                    return templateCacheComponent.getCachedPath(requestPath, siteComponent.getViewNamePreffix(site, domain)
                            + requestPath, metadata.getCacheTime() * 1000, metadata.getAcceptParamters(), site, request, model);
                }
            } else {
                throw new PageNotFoundException(requestPath);
            }
        }
        return requestPath;
    }

    private void billingRequestParamtersToModel(HttpServletRequest request, String acceptParamters, ModelMap model) {
        for (String paramterName : split(acceptParamters, ",")) {
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

    /**
     * 接口请求统一分发
     * 
     * @param callback
     * @return
     */
    @RequestMapping({ "*.json", "/**/*.json" })
    @ResponseBody
    public MappingJacksonValue index(String callback) {
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(NOT_FOUND_MAP);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }
}
