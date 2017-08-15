package org.publiccms.common.view;

import static org.publiccms.common.base.AbstractController.getUserFromSession;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.publiccms.common.base.AbstractFreemarkerView;

/**
 * 
 * WebFreeMarkerView 视图类
 *
 */
public class DefaultWebFreeMarkerView extends AbstractFreemarkerView {
    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        exposeParamters(model,request);
        model.put(CONTEXT_USER, getUserFromSession(request.getSession()));
        super.exposeHelpers(model, request);
    }
}