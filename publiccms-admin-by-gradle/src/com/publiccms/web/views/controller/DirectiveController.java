package com.publiccms.web.views.controller;

import static com.publiccms.web.views.controller.IndexController.INTERFACE_NOT_FOUND;
import static org.springframework.util.StringUtils.uncapitalize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sanluan.common.base.BaseController;
import com.sanluan.common.handler.FreeMarkerExtendHandler;
import com.sanluan.common.handler.HttpParameterHandler;
import com.sanluan.common.servlet.HttpServletDirective;

@Controller
public class DirectiveController extends BaseController {
	private Map<String, HttpServletDirective> actionMap = new HashMap<String, HttpServletDirective>();
	@Autowired
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
	@Autowired
	private FreeMarkerExtendHandler freeMarkerExtendHandler;

	private MediaType mediaType = new MediaType("application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET);

	@RequestMapping("directive.json")
	public void directive(String action, String callback, HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpServletDirective directive = actionMap.get(action);
			if (notEmpty(directive)) {
				directive.execute(mappingJackson2HttpMessageConverter, mediaType, request.getParameterMap(), callback, response);
			} else {
				HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter, mediaType,
						request.getParameterMap(), callback, response);
				handler.put("error", INTERFACE_NOT_FOUND).render();
			}
		} catch (IOException e) {
		} catch (Exception e) {
		}
	}

	@RequestMapping("directives.json")
	@ResponseBody
	public MappingJacksonValue directives(String callback) {
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(actionMap.keySet());
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}

	@Autowired
	public void setActionMap(Map<String, HttpServletDirective> directiveMap) {
		for (Entry<String, HttpServletDirective> entry : directiveMap.entrySet()) {
			String directiveName = uncapitalize(entry.getKey().replaceAll(freeMarkerExtendHandler.getDirectiveRemoveRegex(), ""));
			actionMap.put(directiveName, entry.getValue());
		}
	}
}
