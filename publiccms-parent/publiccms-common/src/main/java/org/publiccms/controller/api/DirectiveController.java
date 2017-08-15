package org.publiccms.controller.api;

import static org.publiccms.common.base.AbstractFreemarkerView.CONTEXT_SITE;
import static org.publiccms.controller.api.ApiController.INTERFACE_NOT_FOUND;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.publiccms.common.base.AbstractController;
import org.publiccms.common.base.AbstractTaskDirective;
import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.component.site.DirectiveComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.directive.BaseTemplateDirective;
import com.publiccms.common.directive.HttpDirective;
import com.publiccms.common.handler.HttpParameterHandler;

/**
 * 
 * DirectiveController 接口指令统一分发
 *
 */
@Controller
public class DirectiveController extends AbstractController {
    private Map<String, BaseTemplateDirective> actionMap = new HashMap<>();
    private List<Map<String, String>> actionList = new ArrayList<>();

    /**
     * 接口指令统一分发
     * 
     * @param action
     * @param callback
     * @param request
     * @param response
     */
    @RequestMapping("directive/{action}")
    public void directive(@PathVariable String action, String callback, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            HttpDirective directive = actionMap.get(action);
            if (null != directive) {
                request.setAttribute(CONTEXT_SITE, getSite(request));
                directive.execute(mappingJackson2HttpMessageConverter, jsonMediaType, request, callback, response);
            } else {
                HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter, jsonMediaType,
                        request, callback, response);
                handler.put(ERROR, INTERFACE_NOT_FOUND);
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
    @RequestMapping("directives")
    @ResponseBody
    public List<Map<String, String>> directives() {
        return actionList;
    }

    /**
     * 接口初始化
     * 
     * @param directiveComponent
     * 
     */
    @Autowired
    public void init(DirectiveComponent directiveComponent) {
        actionMap.putAll(directiveComponent.getTemplateDirectiveMap());
        for (Entry<String, AbstractTemplateDirective> entry : directiveComponent.getTemplateDirectiveMap().entrySet()) {
            Map<String, String> map = new HashMap<>();
            map.put("name", entry.getKey());
            map.put("needAppToken", String.valueOf(entry.getValue().needAppToken()));
            map.put("needUserToken", String.valueOf(entry.getValue().needUserToken()));
            actionList.add(map);
        }
        actionMap.putAll(directiveComponent.getTaskDirectiveMap());
        for (Entry<String, AbstractTaskDirective> entry : directiveComponent.getTaskDirectiveMap().entrySet()) {
            Map<String, String> map = new HashMap<>();
            map.put("name", entry.getKey());
            map.put("needAppToken", String.valueOf(true));
            map.put("needUserToken", String.valueOf(false));
            actionList.add(map);
        }
    }
}
