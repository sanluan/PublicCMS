package org.publiccms.views.method.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

/**
 *
 * GetSleepMethod
 * 
 */
@Component
public class GetSleepMethod extends BaseMethod {

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
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParamtersNumber() {
        return 1;
    }
}
