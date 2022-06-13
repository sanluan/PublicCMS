package com.publiccms.views.method.tools;

import java.util.List;

import com.publiccms.logic.service.tools.HqlService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * GetKeywordsMethod
 * 
 */
@Component
public class GetKeywordsMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        return service.getToken(getString(0, arguments));
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }

    @Resource
    private HqlService service;
}
