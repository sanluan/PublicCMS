package com.publiccms.web.views.controller;

import static com.publiccms.common.constants.FreeMakerConstants.CONTEXT_PATH;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.logic.component.CacheComponent;
import com.sanluan.common.base.BaseController;

/**
 * 
 * IndexController 统一分发Controller
 *
 */
@Controller
public class IndexController extends BaseController {
    @Autowired
    private CacheComponent cacheComponent;

    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    private static final String SEPARATOR = "/";
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
     */
    @RequestMapping({ "index.html", "/", "/**" })
    public String page(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String path = urlPathHelper.getLookupPathForRequest(request);
        if (isNotBlank(path)) {
            if (SEPARATOR.equals(path) || path.endsWith(SEPARATOR)) {
                path += "index.html";
            }
            int index = path.lastIndexOf(".");
            path = path.substring(path.indexOf(SEPARATOR) > 0 ? 0 : 1, index > -1 ? index : path.length());
        }
        model.addAttribute(CONTEXT_PATH, path);
        return cacheComponent.getFilePath(path, request, response, model);
    }

    /**
     * 接口请求统一分发
     * 
     * @param callback
     * @return
     */
    @RequestMapping("*.json")
    @ResponseBody
    public MappingJacksonValue index(String callback) {
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(NOT_FOUND_MAP);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }

    /**
     * 接口请求统一分发
     * 
     * @param callback
     * @return
     */
    @RequestMapping("/**/*.json")
    @ResponseBody
    public MappingJacksonValue dir(String callback) {
        return index(callback);
    }
}
