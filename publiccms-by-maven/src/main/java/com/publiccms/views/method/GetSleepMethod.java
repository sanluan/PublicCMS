package com.publiccms.views.method;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetSleepMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Integer time = getInteger(0, arguments);
        if (notEmpty(time)) {
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }
}
