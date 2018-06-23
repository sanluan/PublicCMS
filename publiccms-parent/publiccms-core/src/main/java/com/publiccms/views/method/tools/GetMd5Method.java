package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.VerificationUtils;

import freemarker.template.TemplateModelException;

/**
 *
 * GetMd5Method
 * 
 */
@Component
public class GetMd5Method extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String string = getString(0, arguments);
        if (CommonUtils.notEmpty(string)) {
            return VerificationUtils.md5Encode(string);
        }
        return null;
    }
    
    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}
