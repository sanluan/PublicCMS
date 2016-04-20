package com.publiccms.views.method;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseMethod;
import static com.sanluan.common.tools.VerificationUtils.encode;

import freemarker.template.TemplateModelException;

@Component
public class GetMd5Method extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String string = getString(0, arguments);
        if (notEmpty(string)) {
            return encode(string);
        }
        return null;
    }
}
