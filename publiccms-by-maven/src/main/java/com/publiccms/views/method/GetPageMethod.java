package com.publiccms.views.method;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetPageMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String url = getString(0, arguments);
        Integer pageIndex = getInteger(1, arguments);
        if (notEmpty(url) && notEmpty(pageIndex)) {
            String prefixFilePath = url.substring(0, url.lastIndexOf('.'));
            if (prefixFilePath.lastIndexOf("_") == prefixFilePath.length() - 1) {
                prefixFilePath = prefixFilePath.substring(0, prefixFilePath.length() - 1);
            }
            String suffixFilePath = url.substring(url.lastIndexOf('.'), url.length());
            if (1 < pageIndex) {
                return prefixFilePath + '_' + pageIndex + suffixFilePath;
            }
        }
        return url;
    }
}
