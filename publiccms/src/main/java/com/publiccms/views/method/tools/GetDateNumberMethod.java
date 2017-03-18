package com.publiccms.views.method.tools;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetDateNumberMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        try {
            Date date = getDate(0, arguments);
            return date.getTime();
        } catch (ParseException e) {
            return null;
        }
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
