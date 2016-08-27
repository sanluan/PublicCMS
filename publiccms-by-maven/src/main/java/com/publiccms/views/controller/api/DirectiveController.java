package com.publiccms.views.controller.api;

import static com.publiccms.logic.component.SiteComponent.CONTEXT_SITE;
import static com.publiccms.views.controller.api.AppController.INTERFACE_NOT_FOUND;
import static org.springframework.util.StringUtils.uncapitalize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.component.TemplateComponent;
import com.sanluan.common.directive.HttpDirective;
import com.sanluan.common.handler.HttpParameterHandler;

/**
 * 
 * DirectiveController 接口指令统一分发
 *
 */
@Controller
public class DirectiveController extends AbstractController {
    private Map<String, AbstractTemplateDirective> actionMap = new HashMap<String, AbstractTemplateDirective>();
    private List<Map<String, String>> actionList = new ArrayList<Map<String, String>>();
    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MediaType mediaType = new MediaType("application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET);

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
            if (notEmpty(directive)) {
                request.setAttribute(CONTEXT_SITE, getSite(request));
                directive.execute(mappingJackson2HttpMessageConverter, mediaType, request, callback, response);
            } else {
                HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter, mediaType, request,
                        callback, response);
                handler.put("error", INTERFACE_NOT_FOUND).render();
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
    @RequestMapping("directives")
    @ResponseBody
    public MappingJacksonValue directives(String callback) {
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(actionList);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }

    /**
     * @param directiveMap
     *            接口初始化
     */
    @Autowired
    public void setActionMap(Map<String, AbstractTemplateDirective> directiveMap) {
        for (Entry<String, AbstractTemplateDirective> entry : directiveMap.entrySet()) {
            String directiveName = uncapitalize(entry.getKey().replaceAll(templateComponent.getDirectiveRemoveRegex(), BLANK));
            actionMap.put(directiveName, entry.getValue());
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", directiveName);
            map.put("needAppToken", String.valueOf(entry.getValue().needAppToken()));
            map.put("needUserToken", String.valueOf(false));
            actionList.add(map);
        }
    }

    @Autowired
    private TemplateComponent templateComponent;
}
