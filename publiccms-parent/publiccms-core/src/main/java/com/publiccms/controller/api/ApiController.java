package com.publiccms.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.HttpParameterHandler;
import com.publiccms.logic.component.site.DirectiveComponent;

/**
 *
 * AppController 接口指令统一分发
 *
 */
@Controller
public class ApiController {
    protected final Log log = LogFactory.getLog(getClass());
    private Map<String, AbstractAppDirective> appDirectiveMap = new HashMap<>();
    private List<Map<String, String>> appList = new ArrayList<>();
    @Autowired
    protected MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
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
    public final static String EXCEPTION = "exception";
    /**
     *
     */
    public static final Map<String, String> NOT_FOUND_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(CommonConstants.ERROR, INTERFACE_NOT_FOUND);
        }
    };

    /**
     * 接口请求统一分发
     *
     * @return result
     */
    @RequestMapping({ CommonConstants.SEPARATOR, "/**" })
    @ResponseBody
    public Map<String, String> api() {
        return NOT_FOUND_MAP;
    }

    /**
     * 接口指令统一分发
     *
     * @param api
     * @param request
     * @param response
     */
    @RequestMapping("{api}")
    public void api(@PathVariable String api, HttpServletRequest request, HttpServletResponse response) {
        try {
            AbstractAppDirective directive = appDirectiveMap.get(api);
            if (null != directive) {
                directive.execute(mappingJackson2HttpMessageConverter, CommonConstants.jsonMediaType, request, response);
            } else {
                HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter,
                        CommonConstants.jsonMediaType, request, response);
                handler.put(CommonConstants.ERROR, INTERFACE_NOT_FOUND).render();
            }
        } catch (Exception e) {
            HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter,
                    CommonConstants.jsonMediaType, request, response);
            try {
                handler.put(CommonConstants.ERROR, EXCEPTION).render();
            } catch (Exception renderException) {
                log.error(renderException.getMessage());
            }
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 接口列表
     *
     * @return result
     */
    @RequestMapping("apis")
    @ResponseBody
    public List<Map<String, String>> apis() {
        return appList;
    }

    /**
     * 接口初始化
     *
     * @param directiveComponent
     * @param directiveList
     *
     */
    @Autowired(required = false)
    public void init(DirectiveComponent directiveComponent, List<AbstractAppDirective> directiveList) {
        for (AbstractAppDirective appDirective : directiveList) {
            if (null == appDirective.getName()) {
                appDirective.setName(directiveComponent.getDirectiveName(appDirective.getClass().getSimpleName()));
            }
            appDirectiveMap.put(appDirective.getName(), appDirective);

            Map<String, String> map = new HashMap<>();
            map.put("name", appDirective.getName());
            map.put("needAppToken", String.valueOf(appDirective.needAppToken()));
            map.put("needUserToken", String.valueOf(appDirective.needUserToken()));
            appList.add(map);
        }
    }
}
