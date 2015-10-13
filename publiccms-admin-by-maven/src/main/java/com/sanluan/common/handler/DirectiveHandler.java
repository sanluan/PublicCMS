package com.sanluan.common.handler;

import static com.sanluan.common.tools.TemplateModelUtils.converBoolean;
import static com.sanluan.common.tools.TemplateModelUtils.converDate;
import static com.sanluan.common.tools.TemplateModelUtils.converDouble;
import static com.sanluan.common.tools.TemplateModelUtils.converInteger;
import static com.sanluan.common.tools.TemplateModelUtils.converLong;
import static com.sanluan.common.tools.TemplateModelUtils.converMap;
import static com.sanluan.common.tools.TemplateModelUtils.converShort;
import static com.sanluan.common.tools.TemplateModelUtils.converString;
import static com.sanluan.common.tools.TemplateModelUtils.converStringArray;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sanluan.common.base.BaseHandler;

import freemarker.core.Environment.Namespace;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class DirectiveHandler extends BaseHandler {
    private Writer out;
    private Map<String, TemplateModel> parameters;
    private TemplateModel[] loopVars;
    private TemplateDirectiveBody templateDirectiveBody;
    private Namespace namespace;
    private ObjectWrapper objectWrapper;

    /**
     * @param environment
     * @param parameters
     * @param templateDirectiveBody
     */
    public DirectiveHandler(Map<String, TemplateModel> parameters, TemplateModel[] loopVars, Writer writer, Namespace namespace,
            ObjectWrapper objectWrapper, TemplateDirectiveBody templateDirectiveBody) {
        this.loopVars = loopVars;
        this.parameters = parameters;
        this.templateDirectiveBody = templateDirectiveBody;
        this.out = writer;
        this.namespace = namespace;
        this.objectWrapper = objectWrapper;
    }

    @Override
    public void render() throws IOException, TemplateException {
        Map<String, TemplateModel> reduceMap = reduce();
        if (null != templateDirectiveBody) {
            templateDirectiveBody.render(out);
        }
        reduce(reduceMap);
    }

    private Map<String, TemplateModel> reduce() throws TemplateModelException {
        Map<String, TemplateModel> reduceMap = new HashMap<String, TemplateModel>();
        for (String key : map.keySet()) {
            if (namespace.containsKey(key)) {
                reduceMap.put(key, namespace.get(key));
            }
            namespace.put(key, objectWrapper.wrap(map.get(key)));
        }
        return reduceMap;
    }

    private void reduce(Map<String, TemplateModel> reduceMap) throws TemplateModelException {
        for (String key : map.keySet()) {
            namespace.remove(key);
        }
        namespace.putAll(reduceMap);
    }

    private TemplateModel getModel(String name) {
        return parameters.get(name);
    }

    public TemplateHashModel getMap(String name) throws TemplateModelException {
        return converMap(getModel(name));
    }

    @Override
    public String getString(String name) throws TemplateModelException {
        return converString(getModel(name));
    }

    @Override
    public Integer getInteger(String name) throws TemplateModelException {
        return converInteger(getModel(name));
    }

    @Override
    public Short getShort(String name) throws TemplateModelException {
        return converShort(getModel(name));
    }

    @Override
    public Long getLong(String name) throws TemplateModelException {
        return converLong(getModel(name));
    }

    @Override
    public Double getDouble(String name) throws TemplateModelException {
        return converDouble(getModel(name));
    }

    @Override
    public String[] getStringArray(String name) throws TemplateModelException {
        return converStringArray(getModel(name));
    }

    @Override
    public Boolean getBoolean(String name) throws TemplateModelException {
        return converBoolean(getModel(name));
    }

    @Override
    public Date getDate(String name) throws TemplateModelException {
        return converDate(getModel(name));
    }

    public TemplateModel[] getLoopVars() {
        return loopVars;
    }
}
