package com.publiccms.common.view;

import java.io.IOException;
import java.util.Map;

import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.TemplateModelUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 *
 * MultiSiteImportDirective
 * 
 */
public class MultiSiteImportDirective implements TemplateDirectiveModel {

    private SysSite site;

    /**
     * @param site
     */
    public MultiSiteImportDirective(SysSite site) {
        this.site = site;
    }

    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVars,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        @SuppressWarnings("unchecked")
        String path = TemplateModelUtils.converString(((Map<String, TemplateModel>) parameters).get("path"));
        @SuppressWarnings("unchecked")
        String namespace = TemplateModelUtils.converString(((Map<String, TemplateModel>) parameters).get("namespace"));
        if (CommonUtils.notEmpty(path) && CommonUtils.notEmpty(namespace) && null != environment) {
            environment.importLib(SiteComponent.getFullTemplatePath(site, path), namespace);
        }
    }
}
