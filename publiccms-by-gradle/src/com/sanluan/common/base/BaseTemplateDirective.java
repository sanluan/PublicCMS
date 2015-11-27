package com.sanluan.common.base;

import java.io.IOException;
import java.util.Map;

import com.sanluan.common.handler.DirectiveHandler;
import com.sanluan.common.handler.RenderHandler;

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
public abstract class BaseTemplateDirective implements TemplateDirectiveModel {
    protected static final String ID = "id";

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVars,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        try {
            execute(new DirectiveHandler(parameters, loopVars, environment.getOut(), environment.getCurrentNamespace(),
                    environment.getObjectWrapper(), templateDirectiveBody));
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateException(e, environment);
        }
    }

    /**
     * @param handler
     * @throws IOException
     * @throws Exception
     */
    public abstract void execute(RenderHandler handler) throws IOException, Exception;
}
