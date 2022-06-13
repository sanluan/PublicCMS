package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * GetRandomMethod
 * 
 */
@Component
public class GetRandomMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Integer max = getInteger(0, arguments);
        if (CommonUtils.notEmpty(max)) {
            return CommonConstants.random.nextInt(max);
        }
        return CommonConstants.random.nextInt();
    }
    
    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 0;
    }
}
