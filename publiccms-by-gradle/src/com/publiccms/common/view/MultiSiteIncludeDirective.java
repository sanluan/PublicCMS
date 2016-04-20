package com.publiccms.common.view;

import static com.publiccms.logic.component.SiteComponent.getFullFileName;
import static com.sanluan.common.tools.TemplateModelUtils.converString;

import java.io.IOException;
import java.util.Map;

import com.publiccms.entities.sys.SysSite;
import com.sanluan.common.base.Base;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class MultiSiteIncludeDirective extends Base implements TemplateDirectiveModel {
    private SysSite site;

    public MultiSiteIncludeDirective(SysSite site) {
        this.site = site;
    }

    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVars,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        @SuppressWarnings("unchecked")
        String path = converString(((Map<String, TemplateModel>) parameters).get("path"));
        if (notEmpty(path) && notEmpty(environment)) {
            environment.include(getFullFileName(site, path), DEFAULT_CHARSET, true);
        }
    }
}
