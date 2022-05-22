package com.publiccms.common.directive;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.publiccms.common.handler.TemplateDirectiveHandler;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 
 * BaseTemplateDirective 模板自定义指令基类
 *
 */
public abstract class BaseTemplateDirective implements TemplateDirectiveModel, Directive, HttpDirective {
    protected final Log log = LogFactory.getLog(getClass());
    private String name;
    private String shortName;
    private String namespace;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVars,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        try {
            execute(new TemplateDirectiveHandler(parameters, loopVars, environment, templateDirectiveBody));
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateException(e, environment);
        }
    }

    /**
     * @return whether to enable http
     */
    public boolean httpEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * @param namespace
     *            the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName
     *            the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
