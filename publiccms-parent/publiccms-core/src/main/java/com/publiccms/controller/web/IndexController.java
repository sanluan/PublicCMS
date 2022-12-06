package com.publiccms.controller.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateCacheComponent;

/**
 * 
 * IndexController 统一分发Controller
 *
 */
@Controller
public class IndexController {
    @Autowired
    private TemplateCacheComponent templateCacheComponent;
    @Autowired
    private LocaleResolver localeResolver;

    /**
     * METADATA页面请求统一分发
     * 
     * @param response
     *            响应
     */
    @RequestMapping({ "/**/" + MetadataComponent.DATA_FILE, "/**/" + MetadataComponent.METADATA_FILE, "/include/*",
            "/**/" + SiteComponent.MODEL_FILE, "/**/" + SiteComponent.CATEGORY_TYPE_FILE, "/**/" + SiteComponent.CONFIG_FILE })
    public void refuse(HttpServletResponse response) {
        try {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
        }
    }

    /**
     * REST页面请求统一分发
     * 
     * @param site
     *            站点
     * @param id
     *            ID
     * @param body
     *            消息体
     * @param request
     *            请求
     * @param response
     *            响应
     * @param model
     *            模型
     * @return view name 视图名
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
     *            站点
     * @param id
     *            ID
     * @param pageIndex
     *            分页
     * @param body
     *            消息体
     * @param request
     *            请求
     * @param response
     *            响应
     * @param model
     *            模型
     * @return view name 视图名
     */
    @RequestMapping({ "/**/{id:[0-9]+}_{pageIndex:[0-9]+}" })
    public String restPage(@RequestAttribute SysSite site, @PathVariable("id") long id,
            @PathVariable("pageIndex") Integer pageIndex, @RequestBody(required = false) String body, HttpServletRequest request,
            HttpServletResponse response, ModelMap model) {
        String requestPath = UrlPathHelper.defaultInstance.getLookupPathForRequest(request);
        if (requestPath.endsWith(CommonConstants.SEPARATOR)) {
            requestPath = requestPath.substring(0, requestPath.lastIndexOf(CommonConstants.SEPARATOR, requestPath.length() - 2))
                    + CommonConstants.getDefaultSubfix();
        } else {
            requestPath = requestPath.substring(0, requestPath.lastIndexOf(CommonConstants.SEPARATOR))
                    + CommonConstants.getDefaultSubfix();
        }
        return templateCacheComponent.getViewName(localeResolver, site, id, pageIndex, requestPath, body, request, response,
                model);
    }

    /**
     * 页面请求统一分发
     * 
     * @param site
     *            当前站点
     * @param body
     *            消息体
     * @param request
     *            请求
     * @param response
     *            响应
     * @param model
     *            模型
     * @return view name 视图名
     */
    @RequestMapping({ CommonConstants.SEPARATOR, "/**" })
    public String page(@RequestAttribute SysSite site, @RequestBody(required = false) String body, HttpServletRequest request,
            HttpServletResponse response, ModelMap model) {
        String requestPath = UrlPathHelper.defaultInstance.getLookupPathForRequest(request);
        if (requestPath.endsWith(CommonConstants.SEPARATOR)) {
            requestPath += CommonConstants.getDefaultPage();
        }
        return templateCacheComponent.getViewName(localeResolver, site, null, null, requestPath, body, request, response, model);
    }

}
