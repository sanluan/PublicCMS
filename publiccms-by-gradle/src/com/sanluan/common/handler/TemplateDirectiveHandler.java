package com.sanluan.common.handler;

import static com.sanluan.common.tools.TemplateModelUtils.converBean;
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
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.sanluan.common.base.BaseHandler;

import freemarker.core.Environment;
import freemarker.core.Environment.Namespace;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class TemplateDirectiveHandler extends BaseHandler {
    private Map<String, TemplateModel> parameters;
    private TemplateModel[] loopVars;
    private TemplateDirectiveBody templateDirectiveBody;
    private Environment environment;

    /**
     * @param environment
     * @param parameters
     * @param templateDirectiveBody
     * @throws Exception
     */
    public TemplateDirectiveHandler(Map<String, TemplateModel> parameters, TemplateModel[] loopVars, Environment environment,
            TemplateDirectiveBody templateDirectiveBody) throws Exception {
        this.parameters = parameters;
        this.loopVars = loopVars;
        this.templateDirectiveBody = templateDirectiveBody;
        this.environment = environment;
        regristerParamters();
    }

    @Override
    public RenderHandler put(String key, Object value) {
        return super.put(key, value);
    }

    @Override
    public void render() throws TemplateException, IOException {
        if (!renderd) {
            Map<String, TemplateModel> reduceMap = reduce();
            if (notEmpty(templateDirectiveBody)) {
                templateDirectiveBody.render(environment.getOut());
            }
            reduce(reduceMap);
            renderd = true;
        }
    }

    @Override
    public void print(String value) throws IOException {
        environment.getOut().write(value);
    }

    private Map<String, TemplateModel> reduce() throws TemplateModelException {
        Map<String, TemplateModel> reduceMap = new LinkedHashMap<String, TemplateModel>();
        ObjectWrapper objectWrapper = environment.getObjectWrapper();
        Namespace namespace = environment.getCurrentNamespace();
        if (loopVars.length >= getSize()) {
            Iterator<Object> iterator = map.values().iterator();
            for (int i = 0; iterator.hasNext(); i++) {
                loopVars[i] = objectWrapper.wrap(iterator.next());
            }
        } else {
            Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                if (i < loopVars.length) {
                    loopVars[i] = objectWrapper.wrap(entry.getValue());
                } else {
                    String key = entry.getKey();
                    if (namespace.containsKey(key)) {
                        reduceMap.put(key, namespace.get(key));
                    }
                    namespace.put(key, objectWrapper.wrap(map.get(key)));
                }
                i++;
            }
        }
        return reduceMap;
    }

    private void reduce(Map<String, TemplateModel> reduceMap) {
        Namespace namespace = environment.getCurrentNamespace();
        for (String key : map.keySet()) {
            namespace.remove(key);
        }
        namespace.putAll(reduceMap);
    }

    private TemplateModel getModel(String name) {
        return parameters.get(name);
    }

    public TemplateHashModelEx getMap(String name) throws TemplateModelException {
        return converMap(getModel(name));
    }

    @Override
    protected String getStringWithoutRegrister(String name) throws TemplateModelException {
        return converString(getModel(name));
    }

    @Override
    public Integer getIntegerWithoutRegrister(String name) throws TemplateModelException {
        return converInteger(getModel(name));
    }

    @Override
    public Short getShort(String name) throws TemplateModelException {
        regristerParamter(PARAMETER_TYPE_SHORT, name);
        return converShort(getModel(name));
    }

    @Override
    public Long getLong(String name) throws TemplateModelException {
        regristerParamter(PARAMETER_TYPE_LONG, name);
        return converLong(getModel(name));
    }

    @Override
    public Double getDouble(String name) throws TemplateModelException {
        regristerParamter(PARAMETER_TYPE_DOUBLE, name);
        return converDouble(getModel(name));
    }

    @Override
    protected String[] getStringArrayWithoutRegrister(String name) throws TemplateModelException {
        return converStringArray(getModel(name));
    }

    @Override
    protected Boolean getBooleanWithoutRegrister(String name) throws TemplateModelException {
        return converBoolean(getModel(name));
    }

    @Override
    public Date getDate(String name) throws TemplateModelException, ParseException {
        regristerParamter(PARAMETER_TYPE_DATE, name);
        return converDate(getModel(name));
    }

    @Override
    public Locale getLocale() throws Exception {
        return environment.getLocale();
    }

    @Override
    public HttpServletRequest getRequest() throws IOException, Exception {
        HttpRequestHashModel httpRequestHashModel = (HttpRequestHashModel) environment.getGlobalVariable("Request");
        if (notEmpty(httpRequestHashModel)) {
            return httpRequestHashModel.getRequest();
        }
        return null;
    }

    @Override
    public Object getAttribute(String name) throws IOException, Exception {
        TemplateModel model = environment.getGlobalVariable(name);
        if (notEmpty(model)) {
            return converBean(model);
        }
        return null;
    }
}