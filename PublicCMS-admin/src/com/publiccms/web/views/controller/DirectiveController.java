package com.publiccms.web.views.controller;

import static com.publiccms.web.views.controller.IndexController.INTERFACE_NOT_FOUND;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private List<String> excludeList = Arrays.asList(new String[] { "systemUserList" });
	private Map<String, HttpServletDirective> actionMap = new HashMap<String, HttpServletDirective>();
	private MediaType mediaType = new MediaType("application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET);
	@Autowired
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

	@RequestMapping("directive.json")
	public void directive(String action, String callback, HttpServletRequest request, HttpServletResponse response) {
		HttpServletDirective directive = actionMap.get(action);
		if (notEmpty(directive)) {
			try {
				directive.execute(mappingJackson2HttpMessageConverter, mediaType, request.getParameterMap(), callback, response);
				return;
			} catch (IOException e) {
			} catch (Exception e) {
			}
		} else {
			HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter, mediaType,
					request.getParameterMap(), callback, response);
			try {
				handler.put("error", INTERFACE_NOT_FOUND).render();
			} catch (IOException e) {
			} catch (Exception e) {
			}
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
	public void setFreeMarkerExtendHandler(FreeMarkerExtendHandler freeMarkerExtendHandler) {
		String prefix = freeMarkerExtendHandler.getDirectivePrefix();
		Map<String, Object> map = freeMarkerExtendHandler.getFreemarkerVariables();
		for (String key : map.keySet()) {
			Object directive = map.get(key);
			if (notEmpty(directive)) {
				key = key.substring(prefix.length());
				if (!excludeList.contains(key) && HttpServletDirective.class.isAssignableFrom(directive.getClass())) {
					actionMap.put(key, (HttpServletDirective) directive);
				}
			}
		}
	}
}
