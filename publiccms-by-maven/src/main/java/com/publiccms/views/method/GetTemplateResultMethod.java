package com.publiccms.views.method;

import static com.sanluan.common.tools.FreeMarkerUtils.makeStringByString;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseMethod;

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

    public GetTemplateResultMethod() {
        configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding(DEFAULT_CHARSET);
        configuration.setTemplateUpdateDelayMilliseconds(0);
        configuration.setAPIBuiltinEnabled(false);
        configuration.setNewBuiltinClassResolver(TemplateClassResolver.ALLOWS_NOTHING_RESOLVER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String template = getString(0, arguments);
        if (notEmpty(template)) {
            template = "<#attempt>" + template + "<#recover><pre>${.error!}</pre></#attempt>";
            try {
                return makeStringByString(template, configuration, null);
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return null;
    }
}
