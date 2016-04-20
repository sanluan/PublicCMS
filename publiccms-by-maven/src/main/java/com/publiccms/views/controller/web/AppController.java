package com.publiccms.views.controller.web;

import static com.publiccms.views.controller.web.IndexController.INTERFACE_NOT_FOUND;
import static org.springframework.util.StringUtils.uncapitalize;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.base.AbstractController;
import com.publiccms.logic.component.TemplateComponent;
import com.sanluan.common.handler.HttpParameterHandler;

/**
 * 
 * AppController 接口指令统一分发
 *
 */
@Controller
public class AppController extends AbstractController {
    private Map<String, Map<String, AbstractAppDirective>> appMap = new LinkedHashMap<String, Map<String, AbstractAppDirective>>();
    private MediaType mediaType = new MediaType("application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET);
    private final static String VERSION_ERROR = "version_error";
    private static final Map<String, String> VERSION_ERROR_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("error", VERSION_ERROR);
        }
    };

    /**
     * 接口指令统一分发
     * 
     * @param action
     * @param callback
     * @param request
     * @param response
     */
    @RequestMapping("app/{version}/api.json")
    public void directive(@PathVariable String version, String action, String callback, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Map<String, AbstractAppDirective> actionMap = appMap.get(version);
            if (notEmpty(actionMap)) {
                AbstractAppDirective directive = actionMap.get(action);
                if (notEmpty(directive)) {
                    directive.execute(mappingJackson2HttpMessageConverter, mediaType, request, callback, response);
                } else {
                    HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter, mediaType,
                            request, callback, response);
                    handler.put("error", INTERFACE_NOT_FOUND).render();
                }
            } else {
                HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter, mediaType, request,
                        callback, response);
                handler.put("error", VERSION_ERROR).render();
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
    @RequestMapping("app/versions.json")
    @ResponseBody
    public MappingJacksonValue directives(String callback) {
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(appMap.keySet());
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }

    /**
     * 接口列表
     * 
     * @param callback
     * @return
     */
    @RequestMapping("app/{version}/apis.json")
    @ResponseBody
    public MappingJacksonValue directives(@PathVariable String version, String callback) {
        MappingJacksonValue mappingJacksonValue;
        Map<String, AbstractAppDirective> actionMap = appMap.get(version);
        if (notEmpty(actionMap)) {
            mappingJacksonValue = new MappingJacksonValue(actionMap.keySet());
        } else {
            mappingJacksonValue = new MappingJacksonValue(VERSION_ERROR_MAP);
        }
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }

    /**
     * @param directiveMap
     *            接口初始化
     */
    @Autowired(required = false)
    public void setAppMap(Map<String, AbstractAppDirective> directiveMap) {
        StringBuffer directives = new StringBuffer();
        int size = 0;
        for (Entry<String, AbstractAppDirective> entry : directiveMap.entrySet()) {
            String directiveName = uncapitalize(entry.getKey().replaceAll(templateComponent.getDirectiveRemoveRegex(), BLANK));
            String version = entry.getValue().getVersion();
            Map<String, AbstractAppDirective> actionMap = appMap.get(version);
            if (empty(actionMap)) {
                actionMap = new HashMap<String, AbstractAppDirective>();
                appMap.put(version, actionMap);
            }
            actionMap.put(directiveName, entry.getValue());
            if (0 != directives.length()) {
                directives.append(",");
            }
            directives.append(directiveName);
            directives.append(" ");
            directives.append(version);
            size++;

        }
        log.info(size + " app directives created:[" + directives.toString() + "];");
    }

    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private TemplateComponent templateComponent;
}
