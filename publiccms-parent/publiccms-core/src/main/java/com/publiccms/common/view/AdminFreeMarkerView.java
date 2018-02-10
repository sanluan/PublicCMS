package com.publiccms.common.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.base.AbstractFreemarkerView;

/**
 * 管理后台 FreeMarker视图类
 * 
 * Admin FreeMarker View Class
 * 
 */
public class AdminFreeMarkerView extends AbstractFreemarkerView {
    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        exposeParamters(model,request);
        model.put(CONTEXT_ADMIN, AbstractController.getAdminFromSession(request.getSession()));
        super.exposeHelpers(model, request);
    }
}