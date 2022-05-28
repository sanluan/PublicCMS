package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.template.TemplateComponent;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * GetHtmlMethod
 * 
 */
@Component
public class GetAbsoluteUrlMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String sitePath = getString(0, arguments);
        String url = getString(1, arguments);
        if (CommonUtils.notEmpty(sitePath) && CommonUtils.notEmpty(url)) {
            return TemplateComponent.getUrl(sitePath, url);
        }
        return null;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 2;
    }
}
