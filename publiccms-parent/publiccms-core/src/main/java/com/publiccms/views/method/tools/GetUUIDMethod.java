package com.publiccms.views.method.tools;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * GetUUIDMethod
 * 
 */
@Component
public class GetUUIDMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        return UUID.randomUUID();
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
