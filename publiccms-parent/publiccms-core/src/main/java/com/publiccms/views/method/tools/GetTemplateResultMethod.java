package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FreeMarkerUtils;

import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

/**
 * @author zhangxd
 * 
 */
@Component
public class GetTemplateResultMethod extends BaseMethod {
    private Configuration configuration;

    /**
     * 
     */
    public GetTemplateResultMethod() {
        configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding(CommonConstants.DEFAULT_CHARSET_NAME);
        configuration.setTemplateUpdateDelayMilliseconds(0);
        configuration.setAPIBuiltinEnabled(false);
        configuration.setNewBuiltinClassResolver(TemplateClassResolver.ALLOWS_NOTHING_RESOLVER);
        configuration.setLogTemplateExceptions(false);
        configuration.setBooleanFormat("c");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String template = getString(0, arguments);
        if (CommonUtils.notEmpty(template)) {
            template = "<#attempt>" + template + "<#recover><pre>${.error!}</pre></#attempt>";
            try {
                return FreeMarkerUtils.generateStringByString(template, configuration, null);
            } catch (Exception e) {
                return e.getMessage();
            }
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
