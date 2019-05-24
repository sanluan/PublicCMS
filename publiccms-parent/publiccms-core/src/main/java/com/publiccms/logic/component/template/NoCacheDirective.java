package com.publiccms.logic.component.template;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.core.DirectiveCallPlace;
import freemarker.core.Environment;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;


public class NoCacheDirective implements TemplateDirectiveModel {
    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] templateModel,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        if (null != templateDirectiveBody) {
            TemplateModel model = environment.getVariable(TemplateCacheComponent.CACHE_VAR);
            if (null != model && model instanceof TemplateBooleanModel) {
                try {
                    DirectiveCallPlace directiveCallPlace = environment.getCurrentDirectiveCallPlace();
                    if (null != directiveCallPlace) {
                        environment.getOut().append(directiveCallPlace.toString());
                    }
                } catch (Exception e) {
                    environment.getOut().append(e.getMessage());
                }
            } else {
                templateDirectiveBody.render(environment.getOut());
            }
        }
    }
}
