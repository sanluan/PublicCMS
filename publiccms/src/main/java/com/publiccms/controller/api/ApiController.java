package com.publiccms.controller.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.base.AbstractController;
import com.publiccms.logic.component.site.DirectiveComponent;
import com.sanluan.common.handler.HttpParameterHandler;

/**
 * 
 * AppController 接口指令统一分发
 *
 */
@Controller
public class ApiController extends AbstractController {
    private Map<String, AbstractAppDirective> appDirectiveMap;
    private List<Map<String, String>> appList = new ArrayList<Map<String, String>>();
    public final static String INTERFACE_NOT_FOUND = "interfaceNotFound";
    public static final Map<String, String> NOT_FOUND_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(ERROR, INTERFACE_NOT_FOUND);
        }
    };

    /**
     * 接口请求统一分发
     * 
     * @param callback
     * @return
     */
    @RequestMapping({ SEPARATOR, "/**" })
    @ResponseBody
    public Map<String, String> api() {
        return NOT_FOUND_MAP;
    }

    /**
     * 接口指令统一分发
     * 
     * @param action
     * @param callback
     * @param request
     * @param response
     */
    @RequestMapping("{api}")
    public void api(@PathVariable String api, String callback, HttpServletRequest request, HttpServletResponse response) {
        try {
            AbstractAppDirective directive = directiveComponent.getAppDirectiveMap().get(api);
            if (null != directive) {
                directive.execute(mappingJackson2HttpMessageConverter, jsonMediaType, request, callback, response);
            } else {
                HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter, jsonMediaType,
                        request, callback, response);
                handler.put(ERROR, INTERFACE_NOT_FOUND).render();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 接口列表
     * 
     * @param callback
     * @return
     */
    @RequestMapping("apis")
    @ResponseBody
    public List<Map<String, String>> apis() {
        return appList;
    }

    /**
     * @param directiveMap
     *            接口初始化
     */
    @Autowired(required = false)
    public void init(Map<String, AbstractAppDirective> directiveMap) {
        appDirectiveMap = directiveComponent.getAppDirectiveMap();
        for (Entry<String, AbstractAppDirective> entry : appDirectiveMap.entrySet()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", entry.getKey());
            map.put("needAppToken", String.valueOf(entry.getValue().needAppToken()));
            map.put("needUserToken", String.valueOf(entry.getValue().needUserToken()));
            appList.add(map);
        }
    }

    @Autowired
    private DirectiveComponent directiveComponent;
}
