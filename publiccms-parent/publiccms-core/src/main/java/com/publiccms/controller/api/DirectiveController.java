package com.publiccms.controller.api;

import static com.publiccms.common.base.AbstractTemplateDirective.APP_TOKEN;
import static com.publiccms.common.base.AbstractTemplateDirective.AUTH_TOKEN;
import static com.publiccms.common.base.AbstractTemplateDirective.AUTH_USER_ID;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jakarta.annotation.Resource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.directive.BaseTemplateDirective;
import com.publiccms.common.handler.HttpParameterHandler;
import com.publiccms.common.tools.JavaDocUtils;
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
    private Map<String, List<Map<String, Object>>> namespaceMap = new TreeMap<>(Comparable::compareTo);
    private List<Map<String, Object>> actionList = new ArrayList<>();
    @Resource
    protected MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Resource
    protected SiteComponent siteComponent;
    protected DirectiveComponent directiveComponent;

    /**
     * 接口指令统一分发
     * 
     * @param action
     * @param appToken
     * @param authToken
     * @param authUserId
     * @param request
     * @param response
     */
    @RequestMapping("directive/{action}")
    public void directive(@PathVariable String action, @RequestHeader(required = false) String appToken,
            @RequestHeader(required = false) String authToken, @RequestHeader(required = false) Long authUserId,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            BaseTemplateDirective directive = actionMap.get(action);
            if (null != directive) {
                if (directive instanceof AbstractTemplateDirective) {
                    if (((AbstractTemplateDirective) directive).needAppToken() && null != appToken) {
                        request.setAttribute(APP_TOKEN, appToken);
                    }
                    if (((AbstractTemplateDirective) directive).needUserToken()) {
                        if (null != authToken) {
                            request.setAttribute(AUTH_TOKEN, authToken);
                        }
                        if (null != authUserId) {
                            request.setAttribute(AUTH_USER_ID, authUserId);
                        }
                    }
                } else {
                    if (null != appToken) {
                        request.setAttribute(APP_TOKEN, appToken);
                    }
                }
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
     * 接口指令统一分发
     * 
     * @param namespace
     * @param directive
     * @param appToken
     * @param authToken
     * @param authUserId
     * @param request
     * @param response
     */
    @RequestMapping("directive/{namespace}/{directive}")
    public void directive(@PathVariable String namespace, @PathVariable String directive,
            @RequestHeader(required = false) String appToken, @RequestHeader(required = false) String authToken,
            @RequestHeader(required = false) Long authUserId, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, BaseTemplateDirective> directiveMap = directiveComponent.getNamespaceMap().get(namespace);
            BaseTemplateDirective d;
            if (null != directiveMap && null != (d = directiveMap.get(directive)) && d.httpEnabled()) {
                if (d instanceof AbstractTemplateDirective) {
                    if (((AbstractTemplateDirective) d).needAppToken() && null != appToken) {
                        request.setAttribute(APP_TOKEN, appToken);
                    }
                    if (((AbstractTemplateDirective) d).needUserToken()) {
                        if (null != authToken) {
                            request.setAttribute(AUTH_TOKEN, authToken);
                        }
                        if (null != authUserId) {
                            request.setAttribute(AUTH_USER_ID, authUserId);
                        }
                    }
                } else {
                    if (null != appToken) {
                        request.setAttribute(APP_TOKEN, appToken);
                    }
                }
                d.execute(mappingJackson2HttpMessageConverter, CommonConstants.jsonMediaType, request, response);
            } else {
                HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter,
                        CommonConstants.jsonMediaType, request, response);
                handler.put(CommonConstants.ERROR, ApiController.INTERFACE_NOT_FOUND).render();
            }
        } catch (Exception e) {
            HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter,
                    CommonConstants.jsonMediaType, request, response);
            try {
                log.error(e.getMessage(), e);
                handler.put(CommonConstants.ERROR, e.getMessage()).render();
            } catch (Exception renderException) {
                log.error(renderException.getMessage());
            }
        }
    }

    /**
     * 接口列表
     * 
     * @return result
     */
    @RequestMapping("directives")
    @ResponseBody
    public List<Map<String, Object>> directives() {
        return actionList;
    }

    /**
     * 接口列表
     * 
     * @return result
     */
    @RequestMapping("namespaces")
    @ResponseBody
    public Map<String, List<Map<String, Object>>> namespaces() {
        return namespaceMap;

    }

    /**
     * 接口初始化
     * 
     * @param directiveComponent
     * 
     */
    @Resource
    public void init(DirectiveComponent directiveComponent) {
        this.directiveComponent = directiveComponent;
        for (Entry<String, Map<String, BaseTemplateDirective>> nameSpaceEntry : directiveComponent.getNamespaceMap().entrySet()) {
            List<Map<String, Object>> namespace = namespaceMap.computeIfAbsent(nameSpaceEntry.getKey(), k -> new ArrayList<>());
            for (Entry<String, BaseTemplateDirective> entry : nameSpaceEntry.getValue().entrySet()) {
                if (entry.getValue().httpEnabled()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", entry.getKey());
                    map.put("shortName", entry.getValue().getShortName());
                    map.put("namespace", entry.getValue().getNamespace());
                    map.put("doc", JavaDocUtils.getClassComment(entry.getValue().getClass().getName()));
                    if (entry.getValue() instanceof AbstractTemplateDirective) {
                        AbstractTemplateDirective directive = (AbstractTemplateDirective) entry.getValue();
                        map.put("needAppToken", directive.needAppToken());
                        map.put("needUserToken", directive.needUserToken());
                        map.put("supportAdvanced", directive.supportAdvanced());
                    } else {
                        map.put("needAppToken", true);
                        map.put("needUserToken", false);
                        map.put("supportAdvanced", false);
                    }
                    actionList.add(map);
                    actionMap.put(entry.getKey(), entry.getValue());
                    namespace.add(map);
                }
            }
            Collections.sort(namespace, (o1, o2) -> Collator.getInstance().compare(o1.get("name"), o2.get("name")));
        }
        Collections.sort(actionList, (o1, o2) -> Collator.getInstance().compare(o1.get("name"), o2.get("name")));
    }
}
