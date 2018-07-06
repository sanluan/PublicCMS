package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModelException;

/**
 *
 * GetSleepMethod
 *
 */
@Component
public class GetSleepMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Integer time = getInteger(0, arguments);
        if (CommonUtils.notEmpty(time) && time <= 60) {
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public boolean httpEnabled() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}
