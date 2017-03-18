package com.publiccms.controller.api;

import static com.publiccms.controller.api.ApiController.NOT_FOUND_MAP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.logic.component.site.DirectiveComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.sanluan.common.base.BaseMethod;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 
 * MethodController 方法统一分发
 *
 */
@RestController
public class MethodController extends AbstractController {
    private Map<String, BaseMethod> methodMap;
    private List<Map<String, String>> methodList = new ArrayList<Map<String, String>>();
    private ObjectWrapper objectWrapper;

    /**
     * 接口指令统一分发
     * 
     * @param method
     * @param callback
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("method/{name}")
    public Object method(@PathVariable String name, String appToken, HttpServletRequest request, HttpServletResponse response) {
        BaseMethod method = methodMap.get(name);
        if (null != method) {
            try {
                if (method.needAppToken()) {
                    SysAppToken token = appTokenService.getEntity(appToken);
                    if (null == token) {
                        return NOT_FOUND_MAP;
                    }
                    SysApp app = appService.getEntity(token.getAppId());
                    if (null == app) {
                        return NOT_FOUND_MAP;
                    }
                }
                String[] paramters = request.getParameterValues("paramters");
                if (notEmpty(paramters) && paramters.length >= method.minParamtersNumber()) {
                    List<TemplateModel> list = new ArrayList<TemplateModel>();
                    for (String paramter : paramters) {
                        list.add(getObjectWrapper().wrap(paramter));
                    }
                    return method.exec(list);
                } else if (empty(paramters) && 0 == method.minParamtersNumber()) {
                    return method.exec(null);
                } else {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(ERROR, "paramtersError");
                    return map;
                }
            } catch (TemplateModelException e) {
                Map<String, String> map = new HashMap<String, String>();
                map.put(ERROR, e.getMessage());
                return map;
            }
        } else {
            return NOT_FOUND_MAP;
        }

    }

    /**
     * 接口列表
     * 
     * @param callback
     * @return
     */
    @RequestMapping("methods")
    public List<Map<String, String>> methods() {
        return methodList;
    }

    /**
     * @param methodMap
     *            接口初始化
     */
    @Autowired
    public void init(DirectiveComponent directiveComponent) {
        methodMap = directiveComponent.getMethodMap();
        for (Entry<String, BaseMethod> entry : methodMap.entrySet()) {
            if (entry.getValue().httpEnabled()) {
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put("name", entry.getKey());
                resultMap.put("minParamters", String.valueOf(entry.getValue().minParamtersNumber()));
                resultMap.put("needAppToken", String.valueOf(entry.getValue().needAppToken()));
                resultMap.put("needUserToken", String.valueOf(false));
                methodList.add(resultMap);
            }
        }
    }

    private ObjectWrapper getObjectWrapper() {
        if (null == objectWrapper) {
            objectWrapper = templateComponent.getWebConfiguration().getObjectWrapper();
        }
        return objectWrapper;
    }

    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysAppService appService;
    @Autowired
    private TemplateComponent templateComponent;
}
