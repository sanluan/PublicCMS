package com.publiccms.common.servlet;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import freemarker.template.ObjectWrapper;
import freemarker.template.ObjectWrapperAndUnwrapper;
import freemarker.template.SimpleCollection;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TemplateHashModel wrapper for a HttpServletRequest attributes.
 */
public final class HttpRequestHashModel implements TemplateHashModelEx {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ObjectWrapper wrapper;

    /**
     * @param request 
     * @param wrapper
     *            Should be an {@link ObjectWrapperAndUnwrapper}, or else some
     *            features might won't work properly. (It's declared as
     *            {@link ObjectWrapper} only for backward compatibility.)
     */
    public HttpRequestHashModel(HttpServletRequest request, ObjectWrapper wrapper) {
        this(request, null, wrapper);
    }

    public HttpRequestHashModel(HttpServletRequest request, HttpServletResponse response, ObjectWrapper wrapper) {
        this.request = request;
        this.response = response;
        this.wrapper = wrapper;
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
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
            keys.add(enumeration.nextElement());
        }
        return new SimpleCollection(keys.iterator(), wrapper);
    }

    @Override
    public TemplateCollectionModel values() {
        List<Object> values = new ArrayList<>();
        for (Enumeration<String> enumeration = request.getAttributeNames(); enumeration.hasMoreElements();) {
            values.add(request.getAttribute(enumeration.nextElement()));
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
