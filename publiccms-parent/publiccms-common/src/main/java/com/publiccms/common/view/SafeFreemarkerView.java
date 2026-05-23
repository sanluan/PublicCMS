package com.publiccms.common.view;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import com.publiccms.common.servlet.SafeRequestContext;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.SimpleHash;

/**
 * SafeFreemarkerView
 * 
 */
public abstract class SafeFreemarkerView extends FreeMarkerView {
    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        model.put(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, new SafeRequestContext(request, response, getServletContext(), model));
        model.put(FreemarkerServlet.KEY_REQUEST, new HttpRequestHashModel(request, response, getObjectWrapper()));
        super.doRender(model, request, response);
    }

    @Override
    protected SimpleHash buildTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        SimpleHash fmModel = super.buildTemplateModel(model, request, response);
        fmModel.remove(FreemarkerServlet.KEY_APPLICATION);
        return fmModel;
    }

    protected void exposeParameters(Map<String, Object> model, HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String parameterName = parameters.nextElement();
            String[] values = request.getParameterValues(parameterName);
            if (CommonUtils.notEmpty(values)) {
                if (1 < values.length) {
                    RequestUtils.removeCRLF(values);
                    model.put(parameterName, values);
                } else {
                    model.put(parameterName, RequestUtils.removeCRLF(values[0]));
                }
            }
        }
    }
}
