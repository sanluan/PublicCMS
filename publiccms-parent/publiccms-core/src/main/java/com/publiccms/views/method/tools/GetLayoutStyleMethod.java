package com.publiccms.views.method.tools;

import java.util.List;
import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.views.pojo.diy.CmsLayout;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * GetDateMethod
 * 
 */
@Component
public class GetLayoutStyleMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        if (arguments.size() >= 2) {
            String style = getString(0, arguments);
            String selector = getString(1, arguments);
            if (CommonUtils.notEmpty(selector) && CommonUtils.notEmpty(style)) {
                Matcher matcher = CmsLayout.SELECTOR_PATTERN.matcher(style);
                return matcher.replaceAll(".diy-layout");
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
        return 2;
    }

}
