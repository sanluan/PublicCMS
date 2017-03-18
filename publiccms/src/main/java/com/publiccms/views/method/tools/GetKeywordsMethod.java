package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.tools.HqlService;
import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetKeywordsMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        return service.getToken(getString(0, arguments));
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParamtersNumber() {
        return 1;
    }

    @Autowired
    private HqlService service;
}
