package com.publiccms.views.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.base.AbstractController;

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
     * @param model
     * @return
     */
    @RequestMapping("/**")
    public String page(HttpServletRequest request, ModelMap model) {
        String path = urlPathHelper.getLookupPathForRequest(request);
        if (notEmpty(path)) {
            if (SEPARATOR.equals(path) || path.endsWith(SEPARATOR)) {
                path += "index.html";
            }
            int index = path.lastIndexOf(".");
            path = path.substring(path.indexOf(SEPARATOR) > 0 ? 0 : 1, index > -1 ? index : path.length());
        }
        return path;
    }
}