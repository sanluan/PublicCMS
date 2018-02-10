package com.publiccms.common.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.base.AbstractFreemarkerView;

/**
 * 
 * WebFreeMarkerView 视图类
 *
 */
public class WebFreeMarkerView extends AbstractFreemarkerView {
    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        model.put(CONTEXT_USER, AbstractController.getUserFromSession(request.getSession()));
        super.exposeHelpers(model, request);
    }
}