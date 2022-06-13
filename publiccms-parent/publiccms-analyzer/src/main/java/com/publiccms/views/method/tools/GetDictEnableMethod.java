package com.publiccms.views.method.tools;

import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.logic.component.site.SiteComponent;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

@Component
public class GetDictEnableMethod extends BaseMethod {
    @Resource
    private SiteComponent siteComponent;

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
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
