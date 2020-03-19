package com.publiccms.common.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.tools.ControllerUtils;

/**
 * 
 * WebFreeMarkerView 视图类
 *
 */
public class WebFreeMarkerView extends AbstractFreemarkerView {
    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        if (!model.containsKey(CONTEXT_USER)) {
            model.put(CONTEXT_USER, ControllerUtils.getUserFromSession(request.getSession()));
        }
        super.exposeHelpers(model, request);
    }
}