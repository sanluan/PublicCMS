package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.logic.component.site.SiteComponent;

import freemarker.template.TemplateModelException;

@Component
public class GetDictEnableMethod extends BaseMethod {
    @Autowired
    private SiteComponent siteComponent;

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        return siteComponent.isDictEnable();
    }

    @Override
    public int minParametersNumber() {
        return 0;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}
