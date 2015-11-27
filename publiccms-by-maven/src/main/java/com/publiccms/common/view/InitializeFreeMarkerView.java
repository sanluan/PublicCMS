package com.publiccms.common.view;

import static com.publiccms.common.constants.FreeMakerConstants.CONTEXT_BASE;
import static freemarker.ext.servlet.FreemarkerServlet.KEY_INCLUDE;
import static org.apache.commons.lang3.ArrayUtils.indexOf;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparator;

import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import freemarker.ext.servlet.IncludePage;

/**
 * 
 * InitializeFreeMarkerView 
 *
 */
public class InitializeFreeMarkerView extends FreeMarkerView {
    protected static final String CONTEXT_ADMIN = "admin";
    protected static final String CONTEXT_USER = "user";
    private String contextPath;
    public static Pattern UNSAFE_PATTERN = Pattern.compile("_.*|get.*|" + CONTEXT_BASE + "|" + KEY_INCLUDE + "|" + CONTEXT_ADMIN + "|"
            + CONTEXT_USER);

    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        model.put(CONTEXT_BASE, getContextPath(request));
        dealRequestParamters(model, request);
        super.exposeHelpers(model, request);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void doRender(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.put(KEY_INCLUDE, new IncludePage(request, response));
        super.doRender(model, request, response);
    }

    private void dealRequestParamters(Map<String, Object> model, HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        String queryString = request.getQueryString();
        if (null == queryString) {
            queryString = "";
        }
        String[] querys = splitByWholeSeparator(queryString, "&");
        String[] urlParameters = new String[querys.length];
        int i = 0;
        for (String query : querys) {
            urlParameters[i++] = splitByWholeSeparator(query, "=")[0];
        }
        while (parameters.hasMoreElements()) {
            String paramterName = parameters.nextElement();
            String[] values = request.getParameterValues(paramterName);
            if (isNotEmpty(values) && !UNSAFE_PATTERN.matcher(paramterName).matches()) {
                if (1 < values.length
                        && (-1 == indexOf(urlParameters, paramterName) || indexOf(urlParameters, paramterName) != indexOf(
                                urlParameters, paramterName))) {
                    model.put(paramterName, values);
                } else {
                    model.put(paramterName, values[0]);
                }
            }
        }
    }

    private String getContextPath(HttpServletRequest request) {
        if (null == contextPath) {
            contextPath = request.getContextPath();
        }
        return contextPath;
    }

}