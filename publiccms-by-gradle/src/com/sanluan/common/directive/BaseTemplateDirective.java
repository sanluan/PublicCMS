package com.sanluan.common.directive;

import java.io.IOException;
import java.util.Map;

import com.sanluan.common.handler.TemplateDirectiveHandler;

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
public abstract class BaseTemplateDirective extends BaseDirective implements TemplateDirectiveModel {
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
}
