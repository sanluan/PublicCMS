package org.publiccms.controller.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.publiccms.common.base.AbstractAppDirective;
import org.publiccms.common.base.AbstractController;
import org.publiccms.logic.component.site.DirectiveComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.handler.HttpParameterHandler;

/**
 * 
 * AppController 接口指令统一分发
 *
 */
@Controller
public class ApiController extends AbstractController {
    private Map<String, AbstractAppDirective> appDirectiveMap;
    private List<Map<String, String>> appList = new ArrayList<>();
    /**
     * 
     */
    public final static String INTERFACE_NOT_FOUND = "interfaceNotFound";
    /**
     * 
     */
    public final static String NEED_APP_TOKEN = "needAppToken";
    /**
     * 
     */
    public final static String UN_AUTHORIZED = "unAuthorized";
    /**
     * 
     */
    public final static String NEED_LOGIN = "needLogin";
    /**
     * 
     */
    public static final Map<String, String> NEED_APP_TOKEN_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(ERROR, NEED_APP_TOKEN);
        }
    };
    /**
     * 
     */
    public static final Map<String, String> NOT_FOUND_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(ERROR, INTERFACE_NOT_FOUND);
        }
    };

    /**
     * 接口请求统一分发
     * 
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
     * @param api
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
     * @return
     */
    @RequestMapping("apis")
    @ResponseBody
    public List<Map<String, String>> apis() {
        return appList;
    }

    /**
     * 接口初始化
     * 
     * @param directiveMap
     * 
     */
    @Autowired(required = false)
    public void init(Map<String, AbstractAppDirective> directiveMap) {
        appDirectiveMap = directiveComponent.getAppDirectiveMap();
        for (Entry<String, AbstractAppDirective> entry : appDirectiveMap.entrySet()) {
            Map<String, String> map = new HashMap<>();
            map.put("name", entry.getKey());
            map.put("needAppToken", String.valueOf(entry.getValue().needAppToken()));
            map.put("needUserToken", String.valueOf(entry.getValue().needUserToken()));
            appList.add(map);
        }
    }

    @Autowired
    private DirectiveComponent directiveComponent;
}
