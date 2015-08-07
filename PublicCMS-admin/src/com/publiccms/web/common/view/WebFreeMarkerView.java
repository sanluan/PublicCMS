package com.publiccms.web.common.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.common.view.InitializeFreeMarkerView;

public class WebFreeMarkerView extends InitializeFreeMarkerView {
	private static final String CONTEXT_USER = "user";

	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
		model.put(CONTEXT_USER, UserUtils.getUserFromSession(request));
		super.exposeHelpers(model, request);
	}
}