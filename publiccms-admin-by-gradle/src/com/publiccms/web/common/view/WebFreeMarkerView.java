package com.publiccms.web.common.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.common.view.InitializeFreeMarkerView;

/**
 * 
 * WebFreeMarkerView 视图类
 *
 */
public class WebFreeMarkerView extends InitializeFreeMarkerView {
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        model.put(CONTEXT_USER, UserUtils.getUserFromSession(request.getSession()));
        super.exposeHelpers(model, request);
    }
}