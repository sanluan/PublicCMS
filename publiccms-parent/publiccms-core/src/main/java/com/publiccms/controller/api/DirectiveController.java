package com.publiccms.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.directive.BaseTemplateDirective;
import com.publiccms.common.directive.HttpDirective;
import com.publiccms.common.handler.HttpParameterHandler;
import com.publiccms.logic.component.site.DirectiveComponent;
import com.publiccms.logic.component.site.SiteComponent;

/**
 * 
 * DirectiveController 接口指令统一分发
 *
 */
@Controller
public class DirectiveController {
    protected final Log log = LogFactory.getLog(getClass());
    private Map<String, BaseTemplateDirective> actionMap = new HashMap<>();
    private List<Map<String, String>> actionList = new ArrayList<>();
    @Autowired
    protected MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    protected SiteComponent siteComponent;

    /**
     * 接口指令统一分发
     * 
     * @param action
     * @param request
     * @param response
     */
    @RequestMapping("directive/{action}")
    public void directive(@PathVariable String action, HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpDirective directive = actionMap.get(action);
            if (null != directive) {
                request.setAttribute(AbstractFreemarkerView.CONTEXT_SITE, siteComponent.getSite(request.getServerName()));
                directive.execute(mappingJackson2HttpMessageConverter, CommonConstants.jsonMediaType, request, response);
            } else {
                HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter,
                        CommonConstants.jsonMediaType, request, response);
                handler.put(CommonConstants.ERROR, ApiController.INTERFACE_NOT_FOUND).render();
            }
        } catch (Exception e) {
            HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter,
                    CommonConstants.jsonMediaType, request, response);
            try {
                handler.put(CommonConstants.ERROR, ApiController.EXCEPTION).render();
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
        for (Entry<String, AbstractTemplateDirective> entry : directiveComponent.getTemplateDirectiveMap().entrySet()) {
            if (entry.getValue().httpEnabled()) {
                Map<String, String> map = new HashMap<>();
                map.put("name", entry.getKey());
                map.put("needAppToken", String.valueOf(entry.getValue().needAppToken()));
                map.put("needUserToken", String.valueOf(entry.getValue().needUserToken()));
                actionList.add(map);
                actionMap.put(entry.getKey(), entry.getValue());
            }
        }
        for (Entry<String, AbstractTaskDirective> entry : directiveComponent.getTaskDirectiveMap().entrySet()) {
            if (entry.getValue().httpEnabled()) {
                Map<String, String> map = new HashMap<>();
                map.put("name", entry.getKey());
                map.put("needAppToken", String.valueOf(true));
                map.put("needUserToken", String.valueOf(false));
                actionList.add(map);
                actionMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
