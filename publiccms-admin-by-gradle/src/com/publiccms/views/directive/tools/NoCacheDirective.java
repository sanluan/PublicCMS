package com.publiccms.views.directive.tools;

import static com.publiccms.common.constants.FreeMakerConstants.CACHE_VAR;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.logging.LogFactory.getLog;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.core.TemplateElement;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 
 * NoCacheDirective 
 *
 */
@SuppressWarnings("deprecation")
@Component
public class NoCacheDirective implements TemplateDirectiveModel {
    protected final Log log = getLog(getClass());

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateDirectiveModel#execute(freemarker.core.
     * Environment, java.util.Map, freemarker.template.TemplateModel[],
     * freemarker.template.TemplateDirectiveBody)
     */
    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] templateModel,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        if (null != templateDirectiveBody) {
            TemplateModel model = environment.getVariable(CACHE_VAR);
            if (null != model && model instanceof TemplateBooleanModel) {
                Class<Environment> clazz = Environment.class;
                try {
                    Method method = clazz.getDeclaredMethod("getInstructionStackSnapshot");
                    method.setAccessible(true);
                    TemplateElement[] elements = (TemplateElement[]) method.invoke(environment);
                    if (isNotEmpty(elements)) {
                        int i = 1;
                        TemplateElement currentElement = elements[elements.length - i];
                        while (currentElement.getClass().getName() != "freemarker.core.UnifiedCall" && i <= elements.length) {
                            i++;
                            currentElement = elements[elements.length - i];
                        }
                        environment.getOut().append(currentElement.getSource());
                    }
                } catch (Exception e) {
                    log.debug(e.getMessage());
                }
            } else {
                templateDirectiveBody.render(environment.getOut());
            }
        }
    }
}
