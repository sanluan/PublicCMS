package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sanluan.common.api.Json;
import com.sanluan.common.base.BaseMethod;

import eu.bitwalker.useragentutils.UserAgent;
import freemarker.template.TemplateModelException;

@Component
public class ParseUserAgentMethod extends BaseMethod implements Json {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        return UserAgent.parseUserAgentString(getString(0, arguments));
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParamtersNumber() {
        return 1;
    }
}
