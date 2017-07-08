package org.publiccms.views.method.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.HtmlUtils.removeHtmlTag;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

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
        if (notEmpty(html)) {
            return removeHtmlTag(html);
        }
        return null;
    }
    
    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParamtersNumber() {
        return 1;
    }
}
