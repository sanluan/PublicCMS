package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.HtmlUtils;

import freemarker.template.TemplateModelException;

/**
 *
 * GetTextFromHtmlMethod
 * 
 */
@Component
public class GetTextFromHtmlMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String html = getString(0, arguments);
        if (CommonUtils.notEmpty(html)) {
            return HtmlUtils.removeHtmlTag(html);
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
