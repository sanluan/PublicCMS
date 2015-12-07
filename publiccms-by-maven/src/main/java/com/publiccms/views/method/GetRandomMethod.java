package com.publiccms.views.method;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetRandomMethod extends BaseMethod {
    Random r = new Random();

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Integer max = getInteger(0, arguments);
        if (null != max) {
            return r.nextInt(max);
        } else {
            return r.nextInt();
        }
    }
}
