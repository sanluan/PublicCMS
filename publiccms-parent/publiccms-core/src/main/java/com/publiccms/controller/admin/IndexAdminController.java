package com.publiccms.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;

/**
 * 
 * IndexAdminController 统一分发Controller
 *
 */
@Controller
public class IndexAdminController extends AbstractController {
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    /**
     * 页面请求统一分发
     * 
     * @param request
     * @return view name
     */
    @RequestMapping("/**")
    public String page(HttpServletRequest request) {
        String path = urlPathHelper.getLookupPathForRequest(request);
        if (CommonUtils.notEmpty(path)) {
            if (CommonConstants.SEPARATOR.equals(path) || path.endsWith(CommonConstants.SEPARATOR)) {
                path += CommonConstants.getDefaultPage();
            }
            int index = path.lastIndexOf(CommonConstants.DOT);
            path = path.substring(path.indexOf(CommonConstants.SEPARATOR) > 0 ? 0 : 1, index > -1 ? index : path.length());
        }
        return path;
    }

    /**
     * 修改语言
     * 
     * @param lang
     * @param returnUrl
     * @param request
     * @param response
     * @return view name
     */
    @RequestMapping("changeLocale")
    public String changeLocale(String lang, String returnUrl, HttpServletRequest request, HttpServletResponse response) {
        if (null != lang) {
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if (null != localeResolver) {
                localeResolver.setLocale(request, response, StringUtils.parseLocaleString(lang));
            }
        }
        if (CommonUtils.empty(returnUrl)) {
            return CommonConstants.TEMPLATE_DONEANDREFRESH;
        } else {
            SysSite site = getSite(request);
            if (isUnSafeUrl(returnUrl, site, request)) {
                returnUrl = site.getDynamicPath();
            }
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
    }
}