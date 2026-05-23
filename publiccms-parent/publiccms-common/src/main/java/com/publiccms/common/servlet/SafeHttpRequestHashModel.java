package com.publiccms.common.servlet;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleCollection;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public final class SafeHttpRequestHashModel implements TemplateHashModelEx {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ObjectWrapper wrapper;
    private static final String[] BLOCKED_PREFIXES = { "org.springframework.web.servlet.DispatcherServlet.CONTEXT",
            "org.springframework.web.context.WebApplicationContext" };

    public SafeHttpRequestHashModel(HttpServletRequest request, ObjectWrapper wrapper) {
        this(request, null, wrapper);
    }

    public SafeHttpRequestHashModel(HttpServletRequest request, HttpServletResponse response, ObjectWrapper wrapper) {
        this.request = request;
        this.response = response;
        this.wrapper = wrapper;
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        if (ArrayUtils.contains(BLOCKED_PREFIXES, key)) {
            return null;
        }
        return wrapper.wrap(request.getAttribute(key));
    }

    @Override
    public boolean isEmpty() {
        return !request.getAttributeNames().hasMoreElements();
    }

    @Override
    public int size() {
        int result = 0;
        for (Enumeration<String> enumeration = request.getAttributeNames(); enumeration.hasMoreElements();) {
            enumeration.nextElement();
            ++result;
        }
        return result;
    }

    @Override
    public TemplateCollectionModel keys() {
        List<String> keys = new ArrayList<>();
        for (Enumeration<String> enumeration = request.getAttributeNames(); enumeration.hasMoreElements();) {
            String key = enumeration.nextElement();
            if (!ArrayUtils.contains(BLOCKED_PREFIXES, key)) {
                keys.add(key);
            }
        }
        return new SimpleCollection(keys.iterator(), wrapper);
    }

    @Override
    public TemplateCollectionModel values() {
        List<Object> values = new ArrayList<>();
        for (Enumeration<String> enumeration = request.getAttributeNames(); enumeration.hasMoreElements();) {
            String key = enumeration.nextElement();
            if (!ArrayUtils.contains(BLOCKED_PREFIXES, key)) {
                values.add(request.getAttribute(key));
            }
        }
        return new SimpleCollection(values.iterator(), wrapper);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public ObjectWrapper getObjectWrapper() {
        return wrapper;
    }
}
