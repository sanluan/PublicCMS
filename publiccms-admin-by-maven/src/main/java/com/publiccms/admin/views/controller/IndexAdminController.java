package com.publiccms.admin.views.controller;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UrlPathHelper;

import com.sanluan.common.base.BaseController;

@Controller
public class IndexAdminController extends BaseController {
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	private static final String SEPARATOR = "/";

	@RequestMapping("/**")
	public String page(HttpServletRequest request, ModelMap model) {
		String path = urlPathHelper.getLookupPathForRequest(request);
		if (isNotBlank(path)) {
			if (SEPARATOR.equals(path) || path.endsWith(SEPARATOR)) {
				path += "index.html";
			}
			int index = path.lastIndexOf(".");
			path = path.substring(path.indexOf(SEPARATOR) > 0 ? 0 : 1, index > -1 ? index : path.length());
		}
		model.addAttribute("path", path);
		return path;
	}
}