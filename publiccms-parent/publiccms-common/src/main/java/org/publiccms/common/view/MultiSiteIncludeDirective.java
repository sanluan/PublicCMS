package org.publiccms.common.view;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.TemplateModelUtils.converString;
import static org.publiccms.logic.component.site.SiteComponent.getFullFileName;

import java.io.IOException;
import java.util.Map;

import org.publiccms.entities.sys.SysSite;

import com.publiccms.common.base.Base;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 *
 * MultiSiteIncludeDirective
 * 
 */
public class MultiSiteIncludeDirective implements TemplateDirectiveModel, Base {

    private SysSite site;

    /**
     * @param site
     */
    public MultiSiteIncludeDirective(SysSite site) {
        this.site = site;
    }

    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVars,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        @SuppressWarnings("unchecked")
        String path = converString(((Map<String, TemplateModel>) parameters).get("path"));
        if (notEmpty(path) && null != environment) {
            environment.include(getFullFileName(site, path), DEFAULT_CHARSET_NAME, true);
        }
    }
}
