package com.publiccms.common.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
        exposeParameters(model, request);
        super.exposeHelpers(model, request);
    }
}