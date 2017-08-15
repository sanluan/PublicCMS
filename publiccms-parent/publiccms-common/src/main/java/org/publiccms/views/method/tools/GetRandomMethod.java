package org.publiccms.views.method.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

/**
 *
 * GetRandomMethod
 * 
 */
@Component
public class GetRandomMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Integer max = getInteger(0, arguments);
        if (notEmpty(max)) {
            return r.nextInt(max);
        }
        return r.nextInt();
    }
    
    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParamtersNumber() {
        return 0;
    }
}
