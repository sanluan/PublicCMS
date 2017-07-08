package org.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import eu.bitwalker.useragentutils.UserAgent;
import freemarker.template.TemplateModelException;

/**
 *
 * ParseUserAgentMethod
 * 
 */
@Component
public class ParseUserAgentMethod extends BaseMethod {

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
