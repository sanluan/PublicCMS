package com.publiccms.common.view.admin;

import static com.publiccms.common.base.AbstractController.getAdminFromSession;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.publiccms.common.view.InitializeFreeMarkerView;

public class AdminFreeMarkerView extends InitializeFreeMarkerView {
    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String paramterName = parameters.nextElement();
            String[] values = request.getParameterValues(paramterName);
            if (isNotEmpty(values)) {
                if (1 < values.length) {
                    model.put(paramterName, values);
                } else {
                    model.put(paramterName, values[0]);
                }
            }
        }
        model.put(CONTEXT_ADMIN, getAdminFromSession(request.getSession()));
        super.exposeHelpers(model, request);
    }
}